package com.zl.appointment.domain.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.zl.appointment.enums.AppointmentStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 预约记录实体
 */

@Data
@TableName("appointment")
public class AppointmentVO {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("schedule_id")
    private Long scheduleId; // 排班ID（关联hospital-service）

    @TableField("patient_id")
    private Long patientId; // 患者ID（关联user-service）

    @TableField("order_no")
    private String orderNo; // 订单号（唯一）

    @TableField("status")
    private AppointmentStatus status; // 预约状态枚举

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

}
