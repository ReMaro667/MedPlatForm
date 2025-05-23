package com.zl.appointment.controller;

import com.zl.appointment.domain.dto.CreateAppointmentDTO;
import com.zl.appointment.service.IAppointmentService;
import com.zl.domain.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api(tags = "预约相关接口")
@RestController
@RequestMapping("/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final IAppointmentService appointmentService;

    @ApiOperation("ai导诊")
    @GetMapping("u/advice")
    public Result<?> advice(String symptom){
        return Result.success(appointmentService.advice(symptom));
    }

    @ApiOperation("预约")
    @PostMapping("u/create")
    public Result<?> create(@RequestBody @Validated CreateAppointmentDTO createAppointmentDTO){
        System.out.println("createAppointmentDTO:"+createAppointmentDTO);
        return appointmentService.create(createAppointmentDTO);
    }

    @ApiOperation("候诊队列")
    @PostMapping("queue/join")
    public Result<?> queueJoin(Long appointmentId){
        return appointmentService.joinQueue(appointmentId);
    }

    @ApiOperation("获取候诊队列")
    @PostMapping("queue/get")
    public Result<?> getQueue(Long appointmentId){
        return appointmentService.getQueue(appointmentId);
    }

    @ApiOperation("叫号")
    @GetMapping("queue/call")
    public Result<?> call(@RequestParam Long scheduleId) {
        return appointmentService.call(scheduleId);
    }

    @ApiOperation("过号")
    @GetMapping("queue/remove")
    public Result<?> queueNext(@RequestParam Long scheduleId,
                               @RequestParam Long appointmentId,
                               @RequestParam String queueNo,
                               @RequestParam int type
    ) {
        return appointmentService.removeQueue(scheduleId,appointmentId, queueNo,type);
    }

    @ApiOperation("就诊中")
    @PutMapping("queue/update")
    public Result<?> update(Long appointmentId,int status){
        appointmentService.updateQueue(appointmentId,status);
        return Result.success();
    }

    @ApiOperation("预约记录查询")
    @GetMapping("{id}")
    public List<CreateAppointmentDTO> record(@PathVariable("id") Long userId){
        return appointmentService.record(userId);
    }

}

