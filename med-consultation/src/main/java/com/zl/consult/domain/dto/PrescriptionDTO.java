package com.zl.consult.domain.dto;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;


@Data
public class PrescriptionDTO {
    @TableId(type = IdType.AUTO)
    private Long prescriptionId;
    private Long consultationId;
    private Long doctorId;
    private String diagnosis;
    private String advice;
}
