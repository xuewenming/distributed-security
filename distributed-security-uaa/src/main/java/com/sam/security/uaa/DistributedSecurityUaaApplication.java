package com.sam.security.uaa;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan(basePackages = {"com.sam.security.uaa.mapper"})
@EnableFeignClients(basePackages = {"com.sam.security.uaa"})
@EnableHystrix
@SpringBootApplication
public class DistributedSecurityUaaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributedSecurityUaaApplication.class, args);
    }

}
