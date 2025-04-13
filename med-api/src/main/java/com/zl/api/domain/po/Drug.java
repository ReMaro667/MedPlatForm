package com.zl.api.domain.po;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Drug {
    private Long drugId;
    private String name;
    private String category;
    private String price;
    private Integer stock;
    private Long pharmacyId;
    private LocalDateTime createdAt;
}
