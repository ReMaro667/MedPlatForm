package com.zl.consult.domain.po;

import com.zl.api.domain.po.Prescription;
import com.zl.enums.StatusEnums;
import lombok.Data;

import java.util.List;

@Data
public class OrderDTO {
    private Long consultationId;
    private Long patientId;
    private Long doctorId;
    private Long appointmentId;
    private String description;
    private String advice;
    private String diagnosis;
    private StatusEnums status = StatusEnums.PENDING;
    private List<Prescription> prescriptions;
    private double total;
}
