package com.zl.pay.controller;

import com.zl.pay.service.PayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/pay")
@RequiredArgsConstructor
public class PayController {
    private final PayService payService;

    @PutMapping("appointment/{id}")
    public void payAppointment(@NotNull @PathVariable("id") Long appointmentId) {
        payService.payAppointment(appointmentId);
    }

    @PutMapping("consult/{id}")
    public void payConsult(@PathVariable("id") @NotNull Long consultId){
        payService.payConsult(consultId);
    }
}
