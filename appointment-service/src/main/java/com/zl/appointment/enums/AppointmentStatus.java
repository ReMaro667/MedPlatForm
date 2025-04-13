package com.zl.appointment.enums;
import com.baomidou.mybatisplus.annotation.IEnum;
public enum AppointmentStatus implements IEnum<String> {
    PENDING("pending"),   // 待支付
    PAID("paid"),         // 已支付
    CANCELLED("cancelled"), // 已取消
    COMPLETED("completed"); // 已完成

    private final String value;

    AppointmentStatus(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
