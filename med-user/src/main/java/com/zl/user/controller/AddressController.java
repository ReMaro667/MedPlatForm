package com.zl.user.controller;

import com.zl.user.service.IAddressService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
@Api(tags = "收货地址管理接口")
public class AddressController {
    private final IAddressService addressService;
}
