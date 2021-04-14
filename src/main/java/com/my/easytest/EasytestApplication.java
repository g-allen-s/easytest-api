package com.my.easytest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan("com.my.easytest.dao")
@SpringBootApplication
//@EnableSwagger2
public class EasytestApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasytestApplication.class, args);
    }

}
