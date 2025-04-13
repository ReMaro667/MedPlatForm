package com.zl.api.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zl.api.enums.PaymentStatus;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 支付订单实体（对应payment-service的数据库表）
 */
@Data
@TableName("payment_order")
public class PaymentOrder {

    @TableId(type = IdType.INPUT) // 订单号由业务生成
    private String orderNo;

    private BigDecimal amount; // 支付金额

    private PaymentStatus status; // 支付状态

    private String payUrl; // 支付链接（支付宝/微信）
}
