package com.zl.appointment.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import com.zl.appointment.enums.AppointmentStatus;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 预约记录实体
 */

@Data
@TableName("appointment")
public class Appointment {

    @TableId(type = IdType.AUTO)
    private Long appointmentId;

    private Long scheduleId; // 排班ID（关联hospital-service）

    private Long patientId; // 患者ID（关联user-service）

    private AppointmentStatus status; // 预约状态枚举

    private LocalDateTime createdAt;

}
