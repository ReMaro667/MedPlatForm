package com.zl.prescription.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("drug")
public class Drug {
    private Long drugId;
    private String name;
    private String category;
    private double price;
    private Integer stock;
    private Long pharmacyId;
    private LocalDateTime createdAt;
}
