package com.zl.appointment.domain.po;

import lombok.Data;

@Data
public class Drug {
    private Long drugId;
    private String name;
    private String category;
    private Integer price;
    private Integer stock;
    private Integer pharmacy_id;
}
