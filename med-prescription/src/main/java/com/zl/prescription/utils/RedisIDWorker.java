package com.zl.prescription.utils;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;


//用于生产ID
@Component
public class RedisIDWorker {

    private static final long BEGIN_TIME = 1640995200L;
    private static final int COUNT_BITS = 32;
    private final StringRedisTemplate stringRedisTemplate;

    public RedisIDWorker(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public long nextId(String KeyPrefix) {
        LocalDateTime now = LocalDateTime.now();
        long nowMillis = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp = (nowMillis - BEGIN_TIME);

        String key = KeyPrefix + ":id";

        String date = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        Long count = stringRedisTemplate.opsForValue().increment("irc:" + key + ":" + date);

        return (timestamp << COUNT_BITS | count);
    }

}
