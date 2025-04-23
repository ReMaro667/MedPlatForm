package com.zl.appointment.service;

import com.zl.appointment.domain.dto.CreateAppointmentDTO;
import com.zl.appointment.domain.po.Department;
import com.zl.domain.Result;
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

    void updateQueue(Long appointmentId, int status);

    Department advice(String symptom);

    Result<?> joinQueue(Long appointmentId);

    Result<?> removeQueue(Long scheduleId,Long appointmentId,String queueNo,int type);

    Result<?> call(Long scheduleId);

    Result<?> getQueue(Long scheduleId);
}
