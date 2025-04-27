package com.zl.user.controller;

import com.zl.user.domain.dto.DoctorRegisterDTO;
import com.zl.user.domain.dto.LoginFormDTO;
import com.zl.user.domain.po.Doctor;
import com.zl.user.domain.vo.DoctorLoginVO;
import com.zl.user.service.IDoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final IDoctorService doctorService;

    @GetMapping("{id}")
    public Doctor getDoctorInfo(@PathVariable("id") Long doctorId) {
        return  doctorService.getById(doctorId);
    }


    //仅用于测试
    @PostMapping("login")
    public DoctorLoginVO login(@RequestBody LoginFormDTO loginFormDTO) {
        return doctorService.login(loginFormDTO);
    }

    //// 注册-----仅用于测试
    @PostMapping("register")
    public void register(@RequestBody DoctorRegisterDTO registerDTO) {
        doctorService.register(registerDTO);
    }
}
