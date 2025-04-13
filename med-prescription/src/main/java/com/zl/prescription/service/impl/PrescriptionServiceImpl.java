package com.zl.prescription.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zl.domain.Result;
import com.zl.prescription.domain.po.Prescription;
import com.zl.prescription.mapper.PrescriptionMapper;
import com.zl.prescription.service.DrugService;
import com.zl.prescription.service.PrescriptionService;
import com.zl.prescription.utils.RedisIDWorker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class PrescriptionServiceImpl extends ServiceImpl<PrescriptionMapper, Prescription> implements PrescriptionService {

}
