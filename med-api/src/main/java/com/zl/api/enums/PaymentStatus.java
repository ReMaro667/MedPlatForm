package com.zl.api.enums;

/**
 * 支付状态
 */
public enum PaymentStatus {
    UNPAID,    // 未支付
    PAID,      // 支付成功
    FAILED,    // 支付失败
    REFUNDED   // 已退款
}
