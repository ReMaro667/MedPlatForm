package com.zl.api.client;

import com.zl.api.domain.po.Prescription;
import com.zl.domain.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@FeignClient(name = "med-prescription")
public interface PrescriptionServiceClient {

    @PostMapping("prescription/save")
    Result<?> save(@RequestBody Prescription prescription);

    @PutMapping("drug/reduce/{id}")
    Result<?> reduce(@PathVariable Long id, @NotNull @RequestParam Integer quantity);

}
