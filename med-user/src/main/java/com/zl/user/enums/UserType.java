package com.zl.user.enums;

import lombok.Getter;

/**
 * 用户角色类型枚举
 */
@Getter
public enum UserType {
    PATIENT("患者"),
    DOCTOR("医生"),
    PHARMACIST("药师"),
    ADMIN("管理员");

    private final String description;

    UserType(String description) {
        this.description = description;
    }

    // 从字符串解析枚举（增强健壮性）
    public static UserType fromString(String value) {
        for (UserType type : values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的用户类型: " + value);
    }
}
