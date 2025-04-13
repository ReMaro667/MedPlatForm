package com.zl.consult.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("consultation")
public class Consult {
    @TableId(type = IdType.AUTO)
    private Long consultationId;
    private Long patientId;
    private Long doctorId;
    private String description;
    private String advice;
}
