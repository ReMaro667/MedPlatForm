package com.zl.api.client;

import com.zl.api.domain.po.PaymentOrder;
import com.zl.domain.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

// 支付服务客户端
public interface PaymentServiceClient {

    @ApiOperation("对预约付款")
    @PostMapping("/pay/appointment/{Id}")
    void Update(@PathVariable("Id")  Long id);
}
