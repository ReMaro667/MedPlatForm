package com.zl.consult;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("com.zl.consult.mapper")
@SpringBootApplication
@EnableFeignClients(basePackages = "com.zl.api.client")
public class ConsultApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsultApplication.class, args);
    }
}