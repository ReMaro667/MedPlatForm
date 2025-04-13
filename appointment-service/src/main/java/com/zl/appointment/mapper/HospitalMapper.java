package com.zl.appointment.mapper;
import com.zl.appointment.domain.po.Department;
import com.zl.appointment.domain.po.Schedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface HospitalMapper {


    @Select("select department_id,name,description,parent_id,created_at,disease from department")
    List<Department> listDepartments();

    @Select("SELECT s.schedule_id,s.department_id,s.doctor_id,d.`name`,s.start_time,s.end_time,s.max_patients,s.work_date " +
            "FROM `schedule` s " +
            "LEFT JOIN doctor d ON s.doctor_id=d.doctor_id " +
            "WHERE s.department_id = #{departmentId} " +
            "AND s.work_date BETWEEN #{startDate} AND DATE_ADD(#{startDate}, INTERVAL 7 DAY)")
    List<Schedule> listSchedules(Long departmentId, LocalDate startDate);

}
