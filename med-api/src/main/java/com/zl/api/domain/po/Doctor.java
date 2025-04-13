package com.zl.api.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@TableName("doctor")
public class Doctor {
    private Long doctorId;
    private Long departmentId;
    private String name;
    private String title;
    private String introduction;
    private String image;
    private String specialty;
    private String licenseNumber;
    private String createdAt;
}
