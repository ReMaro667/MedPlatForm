package com.zl.domain;

import com.zl.exception.CommonException;
import com.zl.utils.ErrorCode;
import lombok.Data;
import lombok.NoArgsConstructor;

// med-common模块中创建
@Data
@NoArgsConstructor
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    // 成功响应（无数据）
    public static <T> Result<T> success() {
        return new Result<>(200, "success", null);
    }

    // 成功响应（带数据）
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }

    // 失败响应（通用错误码）
    public static <T> Result<T> fail(int code, String msg) {
        return new Result<>(code, msg, null);
    }

    // 失败响应（预定义错误枚举）
    public static <T> Result<T> fail(ErrorCode errorCode) {
        return new Result<>(errorCode.getCode(), errorCode.getMsg(), null);
    }
    public static <T> Result<T> error(CommonException e) {
        return new Result<>(e.getCode(), e.getMessage(), null);
    }

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
