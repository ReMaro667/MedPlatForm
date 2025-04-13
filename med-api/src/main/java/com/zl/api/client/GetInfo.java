package com.zl.api.client;

import com.zl.api.domain.po.Doctor;
import com.zl.api.domain.po.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "med-user")
public interface GetInfo {
    @GetMapping("/users/{id}")
    User getUserInfo(@PathVariable("id") Long userId);

    @GetMapping("/doctors/{id}")
    Doctor getDoctorInfo(@PathVariable("id") Long doctorId);

}
