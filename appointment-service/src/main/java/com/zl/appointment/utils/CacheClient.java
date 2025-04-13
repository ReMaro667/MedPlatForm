package com.zl.appointment.utils;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zl.utils.RedisConstants;
import com.zl.utils.RedisData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Slf4j
@Component
@Configuration
public class CacheClient {

    private final StringRedisTemplate stringRedisTemplate;

    public CacheClient(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void setForValue(String key, Object value, Long expireTime, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), expireTime, timeUnit);
    }

    public Map<Object, Object> getForHash(String key) {
        return stringRedisTemplate.opsForHash().entries(key);
    }

    public void setForHash(String key, Map<String,String> value, Long expireTime, TimeUnit timeUnit) {
        stringRedisTemplate.opsForHash().putAll(key, value);
        stringRedisTemplate.expire(key, expireTime, timeUnit);
    }

    public void setWithLocalExpire(String key, Object value, Long expireTime, TimeUnit timeUnit) {
        //设置逻辑过期
        RedisData redisData = new RedisData();
        redisData.setData(value);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(timeUnit.toSeconds(expireTime)));
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }

    //缓存穿透
    public <R, ID> R queryWithPassThrough(
            String KeyPrefix,
            ID id,
            Class<R> type,
            Function<ID, R> dbFallback,
            Long expireTime,
            TimeUnit timeUnit) {
        String key = KeyPrefix+id;

        String Json =  stringRedisTemplate.opsForValue().get(key);

        if(StrUtil.isNotBlank(Json)){
            return JSONUtil.toBean(Json,type);
        }
        if(Json != null){
            return null;
        }

        R r = dbFallback.apply(id);
        if(r == null){
            stringRedisTemplate.opsForValue().set(key, "",5, TimeUnit.MINUTES);
            return null;
        }
        setForValue(key, r, expireTime, timeUnit);
        return r;
    }


    private static final ExecutorService CHECK_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);

    public <R,ID> R queryWithLogicalExpire(
            String KeyPrefix,
            ID id,
            Class<R> type,
            Function<ID,R> dbFallback,
            Long time,
            TimeUnit timeUnit) {
        String key = KeyPrefix+id;

        String Json =  stringRedisTemplate.opsForValue().get(key);
        //判断是否存在
        if(StrUtil.isBlank(Json)){
            //不存在
            return null;
        }
        //命中反序列化
        RedisData redisData = JSONUtil.toBean(Json, RedisData.class);
        R r = JSONUtil.toBean((JSONObject) redisData.getData(), type);
        LocalDateTime expireTime = redisData.getExpireTime();
        //判断是否过期
        if(expireTime.isAfter(LocalDateTime.now())){
            //未过期
            return r;
        }
        //过期
        String LockKey = RedisConstants.LOCK_SHOP_KEY + id;
        Boolean isLock = tryLock(LockKey);
        if(isLock){
            CHECK_REBUILD_EXECUTOR.submit(() -> {
                //重建缓存
                try {
                    //查询数据库
                        R dbData = dbFallback.apply(id);
                        this.setWithLocalExpire(key, r, time, TimeUnit.MINUTES);
                    //设置缓存
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }finally {
                    releaseLock(LockKey);
                }
            });
        }
        return r;
    }




    private boolean tryLock(String key){
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    private void releaseLock(String key){
        stringRedisTemplate.delete(key);
    }

}
