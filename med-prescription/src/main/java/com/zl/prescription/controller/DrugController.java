package com.zl.prescription.controller;

import com.zl.domain.Result;
import com.zl.prescription.domain.po.Drug;
import com.zl.prescription.service.impl.DrugServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/drug")
@RequiredArgsConstructor
public class DrugController {
    private final DrugServiceImpl drugService;

    //减少库存
    @PutMapping("reduce/{id}")
    public Result<?> reduce(@PathVariable Long id,@NotNull @RequestParam Integer quantity) {
        drugService.reduce(id,quantity);
        return Result.success();
    }

    @PostMapping("save")
    public Result<?> save(@RequestBody Drug drug) {
        drugService.save(drug);
        return Result.success();
    }

    @PostMapping("update")
    public Result<?> update(@RequestBody Drug drug) {
        drugService.updateById(drug);
        return Result.success();
    }

    @DeleteMapping("delete/{id}")
    public Result<?> delete(@PathVariable String  id) {
        drugService.removeById(id);
        return Result.success();
    }

    @GetMapping("/get/{id}")
    public Result<?> getById(@PathVariable Long id) {
        return Result.success(drugService.getById(id));
    }

    @GetMapping("getprice/{id}")
    public double getPrice(@RequestParam Long drugId) {
        return drugService.getById(drugId).getPrice();
    }
}
