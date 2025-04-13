package com.zl.appointment.service;


import com.zl.appointment.domain.po.Schedule;
import com.zl.domain.Result;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface HospitalService {

    Result<?> listDepartments();

    Result<?> listSchedules(Long departmentId, String startDate);

}
