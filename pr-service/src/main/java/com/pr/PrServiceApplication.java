package com.pr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//used for non bocking thread along with webflux
//@EnableReactiveFeignClients
//@EnableFeignClients
public class PrServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrServiceApplication.class, args);
    }
}
