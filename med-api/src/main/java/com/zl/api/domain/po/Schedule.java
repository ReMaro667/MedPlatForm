package com.zl.api.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 排班信息实体（对应hospital-service的数据库表）
 */
@Data
@TableName("schedule")
public class Schedule {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long doctorId; // 医生ID

    private LocalDate workDate; // 排班日期

    private LocalTime startTime; // 开始时间

    private LocalTime endTime; // 结束时间

    private Integer maxPatients; // 最大可预约数

    private Integer remaining; // 剩余号源
}
