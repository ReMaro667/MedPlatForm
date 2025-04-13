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
import com.zl.appointment.enums.AppointmentStatus;
import com.zl.appointment.mapper.AppointmentMapper;
import com.zl.appointment.service.IAppointmentService;
import com.zl.domain.Result;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 虎哥
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment> implements IAppointmentService{

    private final AppointmentMapper appointmentMapper;
    private final StringRedisTemplate redisTemplate;
    private final GetInfo getInfo;

    @Override
    @Transactional
    public Result<?> create(CreateAppointmentDTO createAppointmentDTO) {
        Long scheduleId = createAppointmentDTO.getScheduleId();
        LocalDate date = LocalDate.parse(createAppointmentDTO.getDate());
        String cacheKey = getCacheKeyForSchedule(scheduleId,date);
        Schedule schedule = getScheduleByScheduleId(createAppointmentDTO.getDepartmentId(),scheduleId, date);

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
            //更新redis库存
            schedule.setMaxPatients(maxPatients-1);
            String scheduleJson = JSON.toJSONString(schedule);
            redisTemplate.opsForHash().put(cacheKey, scheduleId.toString(), scheduleJson);
            redisTemplate.expire(cacheKey, 10, TimeUnit.MINUTES);

            //更新数据库
            appointmentMapper.updateCount(scheduleId);
            appointmentMapper.createAppointment(createAppointmentDTO);
            return Result.success();
        } catch (InterruptedException e) {
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
    public void payAppointment(Long appointmentId) {
        appointmentMapper.pay(appointmentId);
    }

    @Override
    public void update(Long appointmentId, String status) {
        AppointmentStatus appointmentStatus = AppointmentStatus.valueOf(status);
        appointmentMapper.update(appointmentId, appointmentStatus.getValue());
    }

    @Override
    public Department advice(String symptom) {
        System.out.println("-------------------" + symptom);
        return appointmentMapper.advice(symptom);
    }

    @Override
    public Result<?> queueNext(Long appointmentId,Long departmentId,Long doctorId,Long scheduleId) {
                // 1. 优先处理过号队列
        String key = buildQueueKey(appointmentId);
        System.out.println("Key:"+key);
        Set<String> candidates = redisTemplate.opsForZSet().range(key, 0, 0);
        System.out.println("candidates:"+candidates);
        String queueNo = null;
        if (candidates != null && !candidates.isEmpty()) {
            queueNo = candidates.iterator().next();
            redisTemplate.opsForZSet().remove(key, queueNo);
        }

                // 3. 更新状态
                if (queueNo != null) {
                    String detailKey = "queue:detail:" +scheduleId+":"+ queueNo;
                    redisTemplate.opsForHash().put(detailKey, "status", "2");
                    appointmentMapper.updateQueue(departmentId,doctorId);
                    return Result.success(queueNo+"已更新");
                }
                return Result.success("当前无等候患者");
    }

    public Schedule getScheduleByScheduleId(Long departmentId,Long scheduleId, LocalDate date) {
        // 1. 查询缓存
        String cacheKey = getCacheKeyForSchedule(departmentId, date);
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

    private String getCacheKeyForSchedule(Long departmentId, LocalDate date) {
        return date + ":" + departmentId;
    }

    private String generateQueueNo(Long scheduleId) {
        // 生成日期+序号
        LocalDate now = LocalDate.now();
        String dateStr = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String key = "queue:counter:" + scheduleId +":"+dateStr;
        //从redis获取当前序号
        Long count = redisTemplate.opsForValue().increment(key);
        //设置缓存过期时间
        redisTemplate.expire(key, 10, TimeUnit.MINUTES);
        return "Queue" + dateStr + String.format("%04d", count);
    }


    private String buildQueueKey(Long scheduleId){
        return "queue:"+scheduleId+":";
    }

    @Override
    @Transactional
    public Result<?> joinQueue(Long appointmentId) {
        // 生成排队号（日期+序号）
        Appointment appointment = lambdaQuery().eq(Appointment::getAppointmentId, appointmentId).one();
        Assert.notNull(appointment, "预约不存在");

        Long userId = appointment.getPatientId();
        Long scheduleId = appointment.getScheduleId();
        String key = buildQueueKey(scheduleId);
        Set<String> candidates = redisTemplate.opsForZSet().range(key, 0, 0);
        if (candidates != null)
            return Result.fail(400, "请勿重复挂号");
        System.out.println("candidates:"+candidates);
        //获取当前挂号序号
        //key = "queue:counter:" + scheduleId + ":" +date
        String queueNo = generateQueueNo(scheduleId);//Queue202504070002

        // 存储Redis队列
        //key = "queue:scheduleId"
        // 根据时间戳进行有序排队value = 排队号+时间戳
        String queueKey = buildQueueKey(scheduleId);
        redisTemplate.opsForZSet().add(queueKey, queueNo,System.currentTimeMillis());

        //  存储挂号的详情信息姓名、医生、状态
        String detailKey = "queue:detail:"+scheduleId+":"+ queueNo;
        User userInfo = getInfo.getUserInfo(userId);
//        Doctor doctorInfo = getInfo.getDoctorInfo(scheduleId);
        redisTemplate.opsForHash().putAll(detailKey, Map.of(
                "patientId", String.valueOf(userId),  // 将 userId 转换为 String
                "userName", userInfo.getRealName(),
                "doctorId", String.valueOf(999),  // 将 doctorId 转换为 String
                "doctorName", "doctorName",
                "status", String.valueOf(1)  // 将 status 转换为 String
        ));
        //设置过期时间
        redisTemplate.expire(detailKey, 10L, TimeUnit.MINUTES);

//         写入数据库
        Queue queue = new Queue();
        queue.setQueueNo(queueNo);
        queue.setAppointmentId(appointmentId);
        queue.setDoctorId(999L);
        queue.setPatientId(userId);
        queue.setStatus(1);
        appointmentMapper.queueJoin(queue);
        JoinQueueVo joinQueueVo = new JoinQueueVo();
        joinQueueVo.setUserId(userId);
        joinQueueVo.setNum(queueNo);
        joinQueueVo.setUserName(userInfo.getRealName());
        joinQueueVo.setDoctorName("doctorName");
        joinQueueVo.setDoctorId(999L);
        joinQueueVo.setStatus(1);
        return Result.success(joinQueueVo);
    }

    private boolean tryLock(Long scheduleId) throws InterruptedException {
        String lockKey = "lock:appointmentStock:" + scheduleId;
        int count = 0;
        while (count < 10) {
            try {
                boolean success = Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(lockKey, "1", 10, TimeUnit.SECONDS));
                if (success) {
                    System.out.println("获取锁成功");
                    return true;
                } else {
                    System.out.println("获取锁失败");
                    count++;
                }
            } catch (Exception e) {
                System.err.println("获取锁时发生异常: " + e.getMessage());
                count++;
            }
            Thread.sleep(50); // 可以根据实际情况调整等待时间
        }
        return false;
    }

    // 释放锁的方法
    private void releaseLock(Long scheduleId) {
        String lockKey = "lock:appointmentStock:" + scheduleId;
        try {
            redisTemplate.delete(lockKey);
            System.out.println("释放锁成功");
        } catch (Exception e) {
            System.err.println("释放锁时发生异常: " + e.getMessage());
        }
    }
}
