package com.zl.appointment.service.Impl;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zl.api.client.GetInfo;
import com.zl.api.domain.po.User;
import com.zl.appointment.domain.dto.CreateAppointmentDTO;
import com.zl.appointment.domain.po.Appointment;
import com.zl.appointment.domain.po.Department;
import com.zl.appointment.domain.po.Queue;
import com.zl.appointment.domain.po.Schedule;
import com.zl.appointment.domain.vo.JoinQueueVo;
import com.zl.appointment.domain.vo.QueueVo;
import com.zl.appointment.enums.AppointmentStatus;
import com.zl.appointment.mapper.AppointmentMapper;
import com.zl.appointment.mapper.QueueMapper;
import com.zl.appointment.service.IAppointmentService;
import com.zl.domain.Result;
import com.zl.enums.StatusEnums;
import com.zl.utils.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment> implements IAppointmentService{

    private final AppointmentMapper appointmentMapper;
    private final QueueMapper queueMapper;
    private final StringRedisTemplate redisTemplate;
    private final GetInfo getInfo;
    private final RabbitTemplate rabbitTemplate;
    @Resource
    private RedissonClient redissonClient;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional
    public Result<?> create(CreateAppointmentDTO createAppointmentDTO) {
        //检测重复预约
        Long userId = UserContext.getUser();
        if (redisTemplate.opsForHash().get("appointmentUsers:",userId.toString())!=null){
            return Result.fail(400,"重复预约,当天已预约");
        }
        Long scheduleId = createAppointmentDTO.getScheduleId();
        LocalDate date = LocalDate.parse(createAppointmentDTO.getDate());
        String cacheKey = "<ScheduleList>:"+date +"||departmentId:"+createAppointmentDTO.getDepartmentId();
        Schedule schedule = getCacheByScheduleId(createAppointmentDTO.getDepartmentId(),scheduleId, date);
        System.out.println("schedule:"+schedule);
        if (schedule == null){
            return Result.fail(400,"错误的预约");
        }
        //扣减预约人数
        //获取锁
        try {
            boolean lock = tryLock(scheduleId);
            if (!lock)
                return Result.fail(400,"预约失败");

            //判断库存
            if(schedule.getMaxPatients()<=0)
                return Result.fail(400,"预约已满");

            System.out.println("------schedule:"+schedule);
            int maxPatients = schedule.getMaxPatients();
                //新建缓存
            if (!redisTemplate.opsForHash().putIfAbsent("appointmentUsers:",userId.toString(),"1")){
                return Result.fail(400,"重复预约,当天已预约");
            }
            else {
                //设置晚上12点过期
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime midnight = now.toLocalDate().atStartOfDay().plusDays(1); // 当日晚上12点
                long secondsUntilMidnight = ChronoUnit.SECONDS.between(now, midnight); // 计算时间差
                redisTemplate.expire(
                        "appointmentUser:" + userId,
                        secondsUntilMidnight,
                        TimeUnit.SECONDS
                );
            }
            //更新redis库存
            schedule.setMaxPatients(maxPatients-1);
            String scheduleJson = JSON.toJSONString(schedule);
            redisTemplate.opsForHash().put(cacheKey, scheduleId.toString(), scheduleJson);
            redisTemplate.expire(cacheKey, 30, TimeUnit.MINUTES);
            //更新数据库
            createAppointmentDTO.setPatientId(userId);
            appointmentMapper.updateCount(scheduleId);
            appointmentMapper.createAppointment(createAppointmentDTO);
            try {
                rabbitTemplate.convertAndSend("appointment.topic", "appointment.success",userId);
            } catch (Exception e) {
                log.error("预约成功的消息发送失败，用户id：{}",userId, e);
            }
            return Result.success();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            releaseLock(scheduleId);
        }
    }

    @Override
    public List<CreateAppointmentDTO> record(Long userId) {
        return appointmentMapper.record(userId);
    }

    @Override
    public void updateQueue(Long appointmentId, int status) {
        queueMapper.updateQueue(appointmentId, status);
    }

    @Override
    public Department advice(String symptom) {
        System.out.println("-------------------" + symptom);
        return appointmentMapper.advice(symptom);
    }

    @Override
    @Transactional
    public Result<?> joinQueue(Long appointmentId) {
        // 生成排队号（日期+序号）
        Appointment appointment = lambdaQuery().eq(Appointment::getAppointmentId, appointmentId).one();
        Assert.notNull(appointment, "预约不存在");
        if (Objects.equals(appointment.getStatus(),StatusEnums.PENDING.getValue()))
            return Result.fail(400,"未付款");
        if (Objects.equals(appointment.getStatus(),StatusEnums.COMPLETED.getValue()))
            return Result.success("请勿重复挂号");
        Long userId = appointment.getPatientId();
        Long scheduleId = appointment.getScheduleId();
        //获取当前挂号序号
        String queueNo = generateQueueNo(scheduleId);//Queue202504070002

        // 存储Redis队列
        // 根据时间戳进行有序排队value = 排队号+时间戳
        String queueKey = "queue:"+scheduleId;
        redisTemplate.opsForZSet().add(queueKey, queueNo,System.currentTimeMillis());
        appointmentMapper.update(appointmentId, StatusEnums.COMPLETED.getValue());
        //  存储挂号的详情信息姓名、医生、状态
        String detailKey = "queue:detail:"+scheduleId+":"+ queueNo;
        User userInfo = getInfo.getUserInfo(userId);
        redisTemplate.opsForHash().putAll(detailKey,Map.of(
                "appointmentId",String.valueOf(appointmentId),
                    "queueNo",queueNo,
                "patientId", String.valueOf(userId),  // 将 userId 转换为 String
                "userName", userInfo.getRealName(),
                "status", String.valueOf(1)  // 将 status 转换为 String
        ));
        //写入数据库
        Queue queue = new Queue(appointmentId,queueNo,scheduleId,1);
        appointmentMapper.update(appointmentId, StatusEnums.COMPLETED.getValue());
        queueMapper.queueJoin(queue);
        return Result.success(queue);
    }

    @Override
    public Result<?> getQueue(Long scheduleId) {
        String queueKey = "queue:"+scheduleId;

        Set<String> range = redisTemplate.opsForZSet().range(queueKey, 0, -1);
        if (range == null || range.isEmpty()) {
            return Result.success("暂无信息");
        }
//        ArrayList queueList = new ArrayList<>;
        for (String QueueNo : range) {
            String detailKey = "queue:detail:"+scheduleId+":"+ QueueNo;
            Object appointmentInfo = redisTemplate.opsForHash().entries(detailKey);
//            queueList.add(appointmentInfo);
        }

        String delayKey = "delayQueue:"+scheduleId;
//        Set<String> range1 = redisTemplate.opsForZSet().range(delayKey, 0, -1);
//        QueueVo queueVo = new QueueVo(range, range1);
        return Result.success(range);
    }

    @Override
    public Result<?> call(Long scheduleId) {
        //0 普通队列 1 延迟队列
        // TODO 发送消息通知患者
        String key;
        key = "queue:"+scheduleId;

        Set<String> candidates = redisTemplate.opsForZSet().range(key, 0, 0);
        String queueNo = null;
        if (candidates != null && !candidates.isEmpty()) {
            queueNo = candidates.iterator().next();
            redisTemplate.opsForZSet().remove(key, queueNo);
        }

        if (queueNo != null) {
            String detailKey = "queue:detail:"+scheduleId+":"+ queueNo;
            Object appointmentIdObj = redisTemplate.opsForHash().get(detailKey, "appointmentId");
            redisTemplate.opsForHash().put(detailKey, "status", "2");
            Long appointmentId = null;
            if (appointmentIdObj != null) {
                appointmentId = Long.parseLong((String) appointmentIdObj);
            }
            queueMapper.updateQueue(appointmentId, 2);
            redisTemplate.expire(detailKey, 2, TimeUnit.HOURS);
            //开入延迟队列
            String delayerKey = "delayer:" + scheduleId + ":" + queueNo;
            redisTemplate.opsForValue().set(delayerKey,"");
            redisTemplate.expire(delayerKey, 2, TimeUnit.HOURS);

            String delayQueueKey = "delayQueue:"+scheduleId;
            redisTemplate.opsForSet().add(delayQueueKey, queueNo);
            redisTemplate.expire(delayQueueKey, 2, TimeUnit.HOURS);
            Map<Object, Object> info = redisTemplate.opsForHash().entries(detailKey);
            return Result.success(info);
        }
        return Result.fail(400,"空队列");
    }

    @Override
    public Result<?> removeQueue(Long scheduleId,Long appointmentId,String queueNo,int type) {
        if (type == 0) {
            String queueKey = "queue:" + scheduleId;
            redisTemplate.opsForZSet().remove(queueKey, queueNo);
            String detailKey = "queue:detail:"+scheduleId+":"+ queueNo;
            redisTemplate.opsForHash().delete(detailKey);
        }
        else {
            String delayQueueKey = "delayQueue:" + scheduleId + ":" + queueNo;
            redisTemplate.opsForSet().remove(delayQueueKey,queueNo);
            String detailKey = "queue:detail:"+scheduleId;
            redisTemplate.opsForHash().delete(detailKey);
            String delayerKey = "delayer:" + scheduleId + ":" + queueNo;
            redisTemplate.delete(delayerKey);
        }
        queueMapper.updateQueue(appointmentId,4);
        return Result.success();
    }

    public Schedule getCacheByScheduleId(Long departmentId,Long scheduleId, LocalDate date) {
        // 1. 查询缓存
        String cacheKey = "<ScheduleList>:"+date +"||departmentId:"+departmentId;
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(cacheKey);
        System.out.println("entries:" + entries+"---------"+cacheKey);
        if (!entries.isEmpty()) {
            String scheduleJson = (String) entries.get(scheduleId.toString());
            if (scheduleJson != null) {
                Schedule schedule = JSON.parseObject(scheduleJson, Schedule.class);
                log.debug("命中----------schedule:{}", schedule);
                return schedule;
            }
        }
        return null;
    }

    private String generateQueueNo(Long scheduleId) {
        // 生成日期+序号
        String dateStr = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String key = "queue:counter:" + scheduleId +":"+dateStr;
        //从redis获取当前序号
        Long count = redisTemplate.opsForValue().increment(key);
        //设置缓存过期时间
        redisTemplate.expire(key, 24, TimeUnit.HOURS);
        return dateStr + String.format("%04d", count);
    }

    public boolean tryLock(Long scheduleId) {
        String lockKey = "lock:appointmentStock:" + scheduleId;
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(5, 30, TimeUnit.SECONDS); // 等待0秒，锁持有30秒
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    // 释放锁的方法
    public void releaseLock(Long scheduleId) {
        String lockKey = "lock:appointmentStock:" + scheduleId;
        RLock lock = redissonClient.getLock(lockKey);
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }
}
