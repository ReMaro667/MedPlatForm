package com.zl.appointment.service;

import com.zl.appointment.domain.dto.CreateAppointmentDTO;
import com.zl.appointment.domain.dto.QueueDTO;
import com.zl.appointment.domain.po.Department;
import com.zl.domain.Result;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 虎哥
 * @since 2023-05-05
 */
public interface IAppointmentService {

    Result<?> create(CreateAppointmentDTO createAppointmentDTO);

    List<CreateAppointmentDTO> record(Long userId);

    void payAppointment(Long appointmentId);

    void update(Long appointmentId, String status);

    Department advice(String symptom);

    Result<?> joinQueue(Long appointmentId);

    Result<?> queueNext(Long appointmentId,Long departmentId,Long doctorId,Long screenId);
}
