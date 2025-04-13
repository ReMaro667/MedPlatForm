package com.zl.prescription.controller;

import com.zl.domain.Result;
import com.zl.prescription.domain.po.Prescription;
import com.zl.prescription.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prescription")
@RequiredArgsConstructor
public class PrescriptionController {
    private final PrescriptionService prescriptionService;

    @GetMapping("list")
    public Result<?> list() {
        return Result.success(prescriptionService.list());
    }

    @PostMapping("save")
    public Result<?> save(@RequestBody Prescription prescription) {
        prescriptionService.save(prescription);
        return Result.success(prescription);
    }

    @PostMapping("update")
    public Result<?> update(Prescription prescription) {
        prescriptionService.updateById(prescription);
        return Result.success(prescription);
    }

    @GetMapping("delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        prescriptionService.removeById(id);
        return Result.success();
    }

    @GetMapping("get/{id}")
    public Result<?> get(@PathVariable Long id) {
        return Result.success(prescriptionService.getById(id));
    }
}
