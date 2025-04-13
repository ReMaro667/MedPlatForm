package com.zl.api.domain.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 排班信息DTO（hospital-service返回的数据结构）
 */
@Data
public class ScheduleDTO {

    private Long id;
    private Long doctorId;
    private LocalDate workDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer maxPatients;
    private Integer remaining;
}
