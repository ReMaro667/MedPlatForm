package com.zl.appointment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zl.appointment.domain.dto.CreateAppointmentDTO;
import com.zl.appointment.domain.po.Appointment;
import com.zl.appointment.domain.po.Department;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

@Mapper
public interface AppointmentMapper extends BaseMapper<Appointment> {

    /**
     * 新建预约
     */
    @Insert("insert into appointment (schedule_id, patient_id, status) " +
            "values (#{scheduleId}, #{patientId}, #{status})")
    void createAppointment(CreateAppointmentDTO createAppointmentDTO);

    /**
     * 更新预约余额
     * */
    @Update("UPDATE `schedule` s set s.max_patients = s.max_patients - 1 where s.schedule_id = #{scheduleId} ")
    int updateCount(Long scheduleId);

    /**
     * 获取预约记录
     * 根据用户id
     * */
    @Select("select * from appointment where patient_id = #{userId}")
    List<CreateAppointmentDTO> record(Long userId);

    /**
     * 获取当前剩余预约人数
     * 根据表id
     * */
    @Select("select s.max_patients from schedule s where schedule_id=#{scheduleId}")
    int getCount(Long scheduleId);


    @Update("update appointment a set a.status = #{status} where appointment_id = #{appointmentId}")
    void update(Long appointmentId, String status);

    @Select("select * from department d where d.disease like CONCAT('%', #{name}, '%')")
    Department advice(String symptom);

}
