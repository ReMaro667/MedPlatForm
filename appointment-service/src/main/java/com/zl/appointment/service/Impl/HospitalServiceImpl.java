package com.zl.appointment.service.Impl;

import com.alibaba.fastjson.JSON;
import com.zl.appointment.domain.po.Department;
import com.zl.appointment.domain.po.Schedule;
import com.zl.appointment.mapper.HospitalMapper;
import com.zl.appointment.service.HospitalService;
import com.zl.appointment.utils.CacheClient;
import com.zl.domain.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.*;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import static com.zl.utils.RedisConstants.DEPARTMENT_KEY;

@Slf4j
@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {

    private final HospitalMapper hospitalMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final CacheClient cacheClient;


    @Override
    public Result<?> listDepartments() {
        List<String> departments = stringRedisTemplate.opsForList().range(DEPARTMENT_KEY, 0, -1);
        if (departments != null && !departments.isEmpty()) {
            List<Department> departmentList = new ArrayList<>();
            for (String departmentStr : departments) {
                log.debug("list:{}", departmentStr);
                Department department = JSON.parseObject(departmentStr, Department.class);
                departmentList.add(department);
            }
            System.out.println("命中----------departmentList:" + departmentList);
            return Result.success(departmentList);
        }

        //为空，查询数据库

        List<Department> departmentList = hospitalMapper.listDepartments();
        if (departmentList == null) {
            return Result.fail(400, "获取科室列表失败！！！");
        }
        //集合转为string集合存入redis
        List<String> departmentStrSaveList = new ArrayList<>();
        for (Department department : departmentList) {
            departmentStrSaveList.add(JSON.toJSONString(department));
        }
        stringRedisTemplate.opsForList().rightPushAll(DEPARTMENT_KEY, departmentStrSaveList);
        System.out.println("未命中----------departmentList:" + departmentList);
        return Result.success(departmentList);
    }


    @Override
    public Result<?> listSchedules(Long departmentId, String startDate) {

        List<Schedule> schedules = querySchedulesList(departmentId, LocalDate.parse(startDate));
        if (schedules != null && !schedules.isEmpty()) {
                log.debug("schedules:{}",schedules );
                return Result.success(schedules);
            }
        else return Result.fail(400, "服务异常");
    }

    private List<Schedule> querySchedulesList(Long departmentId, LocalDate date) {
        String cacheKey = date +":"+departmentId;
        // 1. 查询缓存
        Map<Object, Object> entries = cacheClient.getForHash(cacheKey);
        if (!entries.isEmpty()) {
            List<Schedule> scheduleList = new ArrayList<>();
            for (Object key : entries.keySet()) {
                String value = (String) entries.get(key);
                Schedule schedule = JSON.parseObject(value, Schedule.class); // 确保使用正确的 JSON 转换方式
                scheduleList.add(schedule);
            }
            System.out.println("命中----------scheduleList:" + scheduleList);
            return scheduleList;
        }

        // 2. 查询数据库
        List<Schedule> dbList = hospitalMapper.listSchedules(departmentId,date);
        if (dbList == null){
            //设空值
            log.debug("------------------------置空");
            stringRedisTemplate.opsForHash().put(cacheKey, "", "");
            stringRedisTemplate.expire(cacheKey, 100, TimeUnit.SECONDS);
            return null;
        }
        log.debug("--------------------未命中{}",dbList);
        // 3. 构建缓存数据
        Map<String, String> cacheMap = dbList.stream()
                .filter(Objects::nonNull) // 过滤掉 null 值
                .collect(Collectors.toMap(
                        schedule -> schedule.getScheduleId().toString(), // 将 Long 类型的 scheduleId 转换为 String
                        JSON::toJSONString
                ));
        cacheClient.setForHash(cacheKey, cacheMap, 10L, TimeUnit.MINUTES);
        return dbList;
    }
    }