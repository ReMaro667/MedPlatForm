package com.zl.user.domain.vo;

import lombok.Data;

@Data
public class DoctorLoginVO {
    private String token;
    private String username;
    private Long userId;
    private String phone;
    private String role;
}
