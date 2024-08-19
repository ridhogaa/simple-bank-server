package org.k1.simplebankapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class SimpleBankApplication {

    @PostConstruct
    public void init() {
        // Setting the default timezone to Asia/Bangkok (GMT+7)
        java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone("Asia/Bangkok"));
    }

    public static void main(String[] args) {
        SpringApplication.run(SimpleBankApplication.class, args);
    }

}
