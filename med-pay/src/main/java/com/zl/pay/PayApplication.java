package com.zl.pay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.zl.pay.mapper")
@SpringBootApplication
public class PayApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(PayApplication.class, args);
    }
}
