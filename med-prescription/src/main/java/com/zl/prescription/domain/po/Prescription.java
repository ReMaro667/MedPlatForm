package com.zl.prescription.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Prescription {
    @TableId(type = IdType.AUTO)
    private Long prescriptionId;
    private Long consultationId;
    private Long pharmacistId;
    private Long drugId;
    private LocalDateTime createdAt;
}
