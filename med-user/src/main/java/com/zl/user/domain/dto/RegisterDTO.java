package com.zl.user.domain.dto;

import com.zl.user.enums.UserType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class RegisterDTO {

    @NotBlank(message = "名字不能为空")
    @Size(min = 2, max = 10, message = "用户名长度2-10位")
    private String name;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
    private String phone;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 16, message = "密码长度8-20位")
    private String password;

    // 可选：短信验证码（如果开启短信验证）
    private String smsCode;

    // 默认注册为患者用户
    private UserType userType = UserType.PATIENT;
}
