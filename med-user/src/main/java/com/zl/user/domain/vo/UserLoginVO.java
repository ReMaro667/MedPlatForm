package com.zl.user.domain.vo;

import lombok.Data;

@Data
public class UserLoginVO {
    private String token;
    private String username;
    private Long userId;
    private String phone;
}
