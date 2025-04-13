package com.zl.appointment.domain.dto;

import lombok.Data;

@Data
public class QueueDTO {
    private Long appointmentId;
    private Long userId;
    private Long doctorId;
    private Long departmentId;
}
