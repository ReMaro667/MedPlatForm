package com.zl.api.client;

import com.zl.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "med-prescription")
public interface DrugServiceClient {
    @GetMapping("drug/getprice/{id}")
    public double getPrice(@PathVariable Long id);
}
