package com.zl.consult.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zl.api.client.PrescriptionServiceClient;
import com.zl.api.domain.po.Prescription;
import com.zl.consult.domain.po.OrderDTO;
import com.zl.consult.mapper.ConsultMapper;
import com.zl.consult.service.ConsultService;
import com.zl.consult.domain.po.Consult;
import com.zl.consult.utils.RedisIDWorker;
import com.zl.domain.Result;
import com.zl.utils.CacheClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ConsultServiceImpl extends ServiceImpl<ConsultMapper, Consult> implements ConsultService {

    private final ConsultMapper consultMapper;
    private final RedisIDWorker redisIDWorker;

    private final PrescriptionServiceClient prescriptionServiceClient;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> create(OrderDTO orderDTO) {
        long id = redisIDWorker.nextId("consult:");
        System.out.println("consult:"+orderDTO);
        orderDTO.setConsultationId(id);
        Consult consult = new Consult();
        consult.setConsultationId(id);
        consult.setPatientId(orderDTO.getPatientId());
        consult.setDoctorId(orderDTO.getDoctorId());
        consult.setDescription(orderDTO.getDescription());
        consult.setAdvice(orderDTO.getAdvice());
        consultMapper.insert(consult);
        //保存Prescription
        for (Prescription prescription : orderDTO.getPrescriptions()) {
            prescription.setConsultationId(id);
            System.out.println("prescription:"+prescription);
            try {
                prescriptionServiceClient.save(prescription);
                prescriptionServiceClient.reduce(prescription.getDrugId(), prescription.getQuantity());
            }catch (Exception e){
                throw new RuntimeException("服务异常");
            }
        }
        return Result.success(orderDTO);
    }
}
