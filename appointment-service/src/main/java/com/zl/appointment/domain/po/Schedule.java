package com.zl.appointment.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Time;
import java.time.LocalDate;

@Data
public class Schedule {

    @TableId(type = IdType.AUTO)
    private  Long scheduleId;

    private Long doctorId;

    private Long departmentId;

    private  Long hospitalId;

    private String name;

    private LocalDate workDate;

    private  Time startTime;

    private Time endTime;

    private int maxPatients;
}
