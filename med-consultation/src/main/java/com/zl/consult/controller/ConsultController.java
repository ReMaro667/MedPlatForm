package com.zl.consult.controller;

import com.zl.consult.domain.po.OrderDTO;
import com.zl.consult.service.ConsultService;
import com.zl.domain.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "问诊相关接口")
@RestController
@RequestMapping("/consult")
@RequiredArgsConstructor
public class ConsultController {

    private final ConsultService consultService;


    @PostMapping("create")
    public Result<?> create (@RequestBody OrderDTO orderDTO) {
         return consultService.create(orderDTO);
    }

}
