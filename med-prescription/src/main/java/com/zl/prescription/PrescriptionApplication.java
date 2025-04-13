package com.zl.prescription;

import com.zl.utils.CacheClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@MapperScan("com.zl.prescription.mapper")
@SpringBootApplication
@EnableFeignClients(basePackages = "com.zl.api.client")
@Import({ CacheClient.class })
public class PrescriptionApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrescriptionApplication.class, args);
    }

}
