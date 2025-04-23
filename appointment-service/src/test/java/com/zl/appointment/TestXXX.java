package com.zl.appointment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class TestXXX {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void flushAllDatabases() {
        if (redisTemplate != null) {
            redisTemplate.getConnectionFactory().getConnection().flushAll();
            System.out.println("所有数据库已清空");
        }
    }

}
