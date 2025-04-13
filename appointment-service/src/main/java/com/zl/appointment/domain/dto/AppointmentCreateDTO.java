package com.zl.appointment.domain.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;

/**
 * 预约创建请求参数
 */
@Data
public class AppointmentCreateDTO {

    @NotNull(message = "排班ID不能为空")
    private Long scheduleId;

    @NotNull(message = "患者ID不能为空")
    private Long patientId;
}
