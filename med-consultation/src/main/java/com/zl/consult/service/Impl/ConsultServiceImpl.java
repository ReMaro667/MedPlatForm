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
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@GlobalTransactional
public class ConsultServiceImpl extends ServiceImpl<ConsultMapper, Consult> implements ConsultService {

    private final ConsultMapper consultMapper;
    private final RedisIDWorker redisIDWorker;

    private final PrescriptionServiceClient prescriptionServiceClient;
    @Override
    public Result<?> create(OrderDTO orderDTO) {
        long id = redisIDWorker.nextId("consult:");
        orderDTO.setConsultationId(id);
        Consult consult = new Consult(orderDTO);
        consult.setConsultationId(id);
        //保存Prescription
        double total=0;
        for (Prescription prescription : orderDTO.getPrescriptions()) {
            prescription.setConsultationId(id);
            Long drugId = prescription.getDrugId();
            double price = prescriptionServiceClient.getPrice(drugId);
            int num = prescription.getQuantity();
            total+=num*price;
            System.out.println("prescription:"+prescription);
            try {
                //可优化
                prescriptionServiceClient.reduce(prescription.getDrugId(), prescription.getQuantity());
                prescriptionServiceClient.save(prescription);
                //发送通知药房采药
            }catch (Exception e){
                throw new RuntimeException("服务异常");
            }
        }
        orderDTO.setTotal(total);
        consult.setTotal(total);
        consultMapper.insert(consult);
        return Result.success(orderDTO);
    }
}
