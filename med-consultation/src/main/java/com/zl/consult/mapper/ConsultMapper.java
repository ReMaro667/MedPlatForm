package com.zl.consult.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zl.consult.domain.po.Consult;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ConsultMapper extends BaseMapper<Consult> {

    @Insert("insert into consultation (consultation_id,patient_id,doctor_id,description,advice) values" +
            " (#{consultId},#{patientId},#{doctorId},#{description},#{advice}) ")
    int create(Consult consult);

}
