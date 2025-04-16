package com.zl.prescription.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zl.domain.Result;
import com.zl.exception.BizIllegalException;
import com.zl.prescription.domain.po.Drug;
import com.zl.prescription.mapper.DrugMapper;
import com.zl.prescription.service.DrugService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DrugServiceImpl extends ServiceImpl<DrugMapper, Drug> implements DrugService {
    private final DrugMapper drugMapper;
    private final StringRedisTemplate stringRedisTemplate;
    //减少药品库存
    @Override
    @Transactional
    public Result<?> reduce(Long drugId, Integer quantity) {
        //抛出异常
        // 尝试从Redis中扣减库存
        String cacheKey = "drug:stock:" + drugId;
        Integer stock;
        String s = stringRedisTemplate.opsForValue().get(cacheKey);
        if (s != null && !s.isEmpty()) {
            stock = Integer.parseInt(s);
            System.out.println("stock:"+stock+"  quantity:"+quantity);
            if (stock >= quantity) {
                // 缓存中库存充足，扣减缓存
                stringRedisTemplate.opsForValue().decrement(cacheKey, quantity);
                //扣减数据库
                System.out.println("扣减库存");
                drugMapper.reduce(drugId, quantity);
                return Result.success();
            } else {
                //抛出异常
                throw new BizIllegalException("库存不足");
                // 如果缓存扣减失败，抛出异常或记录日志
            }
        }
        // 从数据库中获取库存
        Drug drug = lambdaQuery().eq(Drug::getDrugId,drugId ).one();
        System.out.println("----------drug:"+drug);
        stock = drug.getStock();
        //存入Redis
        stringRedisTemplate.opsForValue().set(cacheKey, stock.toString());
        //设置过期时间
        stringRedisTemplate.expire(cacheKey, 10, java.util.concurrent.TimeUnit.MINUTES);
        if (stock >= quantity) {
            // 缓存中库存充足，扣减缓存
            stringRedisTemplate.opsForValue().decrement(cacheKey, quantity);
            //设置过期时间
            stringRedisTemplate.expire(cacheKey, 10, java.util.concurrent.TimeUnit.MINUTES);
            //扣减数据库
            drugMapper.reduce(drugId, quantity);
            return Result.success();
        } else {
            // 如果缓存扣减失败，抛出异常或记录日志
            throw new BizIllegalException("库存不足");
        }
    }

    @Override
    public double getPrice(Long drugId) {
        Drug drug = lambdaQuery().eq(Drug::getDrugId,drugId ).one();
        return drug.getPrice();
    }
}