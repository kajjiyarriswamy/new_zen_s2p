package com.pr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//used for non bocking thread along with webflux
//@EnableReactiveFeignClients
//@EnableFeignClients
@EnableScheduling
public class PrServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrServiceApplication.class, args);
    }
}
