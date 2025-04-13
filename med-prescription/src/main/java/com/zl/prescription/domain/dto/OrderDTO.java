package com.zl.prescription.domain.dto;

import com.zl.enums.StatusEnums;
import com.zl.prescription.domain.po.Prescription;
import lombok.Data;

import java.util.List;

@Data
public class OrderDTO {
    private Long consultationId;
    private Long patientId;
    private Long doctorId;
    private String description;
    private String advice;
    private String diagnosis;
    private StatusEnums status;
    private List<Prescription> drugs;

}
