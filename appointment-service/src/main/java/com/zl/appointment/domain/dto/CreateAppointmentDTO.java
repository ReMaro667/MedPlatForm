package com.zl.appointment.domain.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.zl.appointment.enums.AppointmentStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 预约记录实体
 */

@Data
@TableName("appointment")
public class CreateAppointmentDTO {

    @TableId(type = IdType.AUTO)
    private Long appointmentId;

    @NotNull
    private Long departmentId;
    @NotNull
    private Long scheduleId; // 排班ID（关联hospital-service）

//    private Long patientId; // 患者ID（关联user-service）

    @NotNull
    private AppointmentStatus status; // 预约状态枚举

    @NotNull
    String date;
}
