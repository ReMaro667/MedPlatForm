package com.zl.user.domain.po;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserInfo {
    private Long userId;
    private String role;
}
