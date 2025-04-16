package com.zl.pay.mapper;

import org.apache.ibatis.annotations.Update;
import org.mapstruct.Mapper;

@Mapper
public interface PayMapper {

    @Update("update appointment set status = 'paid' where appointment_id = #{payId}")
    void appointmentPay(Long payId);

    @Update("update consult set status = 'paid' where consult_Id = #{consultId}")
    void consultPay(Long consultId);
}
