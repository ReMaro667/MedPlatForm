package com.zl.utils;

import lombok.Getter;

public enum ErrorCode {
    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权访问"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    INTERNAL_ERROR(500, "系统内部错误"),

    // 业务相关错误码（按需扩展）
    USER_EXIST(1001, "用户已存在"),
    PHONE_REGISTERED(1002, "手机号已注册"),
    INVALID_CREDENTIAL(1003, "用户名或密码错误");

    @Getter
    private final int code;
    @Getter
    private final String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
