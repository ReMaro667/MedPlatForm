package com.zl.user.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zl.user.enums.UserType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class DoctorRegisterDTO {

    @NotNull
    @NotBlank
    private Long doctorId;

    @NotBlank(message = "名字不能为空")
    @Size(min = 2, max = 10, message = "用户名长度2-10位")
    private String name;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
    private String phone;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 16, message = "密码长度8-20位")
    private String password;

    @NotBlank(message = "科室id不能为空")
    private Long departmentId;
}
