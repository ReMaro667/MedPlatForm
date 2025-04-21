package com.zl.user.controller;

import com.zl.user.domain.po.Doctor;
import com.zl.user.service.IDoctorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final IDoctorService doctorService;

    public DoctorController(IDoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("{id}")
    public Doctor getDoctorInfo(@PathVariable("id") Long doctorId) {
        return  doctorService.getById(doctorId);
    }
}
