package com.zl.consult.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zl.consult.domain.po.Consult;
import com.zl.consult.domain.po.OrderDTO;
import com.zl.domain.Result;

public interface ConsultService extends IService<Consult> {

    Result<?> create(OrderDTO orderDTO);

}
