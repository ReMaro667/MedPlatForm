package com.zl.prescription.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zl.prescription.domain.po.Drug;
import org.apache.ibatis.annotations.Update;


public interface DrugMapper extends BaseMapper<Drug> {
    // 减库存
    @Update("update drug set stock = stock - #{quantity} where drug_id = #{drugId}")
    void   reduce(Long drugId, Integer quantity);

}
