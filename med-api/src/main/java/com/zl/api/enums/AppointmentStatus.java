package com.zl.api.enums;

/**
 * 预约状态
 */
public enum AppointmentStatus {
    PENDING,   // 待支付
    PAID,      // 已支付
    CANCELLED, // 已取消
    COMPLETED  // 已完成（就诊结束）
}
