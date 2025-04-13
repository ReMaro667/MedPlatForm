package com.zl.api.domain.po;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Prescription {
    private Long prescriptionId;
    private Long consultationId;
    private Long pharmacistId;
    private Long drugId;
    private Integer quantity;
    private LocalDateTime createdAt;
}
