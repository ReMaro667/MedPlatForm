package com.zl.pay.service.impl;

import com.zl.pay.mapper.PayMapper;
import com.zl.pay.service.PayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class PayServiceImpl implements PayService {
    private final PayMapper payMapper;
    @Override
    public void payAppointment(Long appointmentId) {
        System.out.println("appointmentId:"+appointmentId);
        //修改数据库中的预约状态为已付款
        payMapper.appointmentPay(appointmentId);
        System.out.println("修改数据库中的预约状态为已付款");
    }

    @Override
    public void payConsult(Long consultId) {
        System.out.println("consultId:"+consultId);
        //修改数据库中的预约状态为已付款
        payMapper.consultPay(consultId);
        System.out.println("修改数据库中的预约状态为已付款");
    }
}
