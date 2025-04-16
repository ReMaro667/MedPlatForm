package com.zl.prescription.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zl.domain.Result;
import com.zl.prescription.domain.po.Drug;

public interface DrugService extends IService<Drug> {
    Result<?> reduce(Long id, Integer quantity);
    double getPrice(Long drugId);
}
