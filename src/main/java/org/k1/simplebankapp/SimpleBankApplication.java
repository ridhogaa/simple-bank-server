package org.k1.simplebankapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class SimpleBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleBankApplication.class, args);
    }

}
