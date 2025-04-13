package com.zl.appointment.controller;


import com.zl.appointment.service.HospitalService;
import com.zl.domain.Result;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Api(tags = "医院相关接口")
@RestController
@RequestMapping
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;
    /**
    * 获取医院科室信息
    * */
    @GetMapping("/department")
    public Result<?> listDepartments() {

        return hospitalService.listDepartments();
    }

    /**
    * 获取医院排班信息
    * */

    @GetMapping("/schedule/{id}&{startDate}")
    public Result<?> listSchedules(@PathVariable("id") Long departmentId,@PathVariable("startDate") String startDate) {
        return hospitalService.listSchedules(departmentId,startDate);
    }
}
