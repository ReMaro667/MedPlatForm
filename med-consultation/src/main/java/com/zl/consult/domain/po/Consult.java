package com.zl.consult.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("consultation")
public class Consult {
    @TableId(type = IdType.AUTO)
    private Long consultationId;
    private Long patientId;
    private Long appointmentId;
    private double total;
    private Long doctorId;
    private String description;
    private String advice;

    public Consult(OrderDTO orderDTO){
        this.patientId = orderDTO.getPatientId();
        this.appointmentId = orderDTO.getAppointmentId();
        this.doctorId = orderDTO.getDoctorId();
        this.description = orderDTO.getDescription();
        this.advice = orderDTO.getAdvice();
        this.total = orderDTO.getTotal();
    }
    public Consult(){}
}
