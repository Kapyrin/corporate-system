package com.example.timetrackingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.example.timetrackingservice.client")
@SpringBootApplication
public class TimeTrackingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimeTrackingServiceApplication.class, args);
    }

}
