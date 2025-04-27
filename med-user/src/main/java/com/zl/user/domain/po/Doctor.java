package com.zl.user.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
    @TableId(value = "doctor_id",type = IdType.AUTO)
    private Long doctorId;
    private Long departmentId;
    private String name;
    private String password;
    private String title;
    protected String phone;
    private String introduction;
    private String specialty;
    private String licenseNumber;
    private String createdAt;
}
