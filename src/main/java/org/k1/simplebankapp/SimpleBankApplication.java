package org.k1.simplebankapp;

import org.k1.simplebankapp.config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class SimpleBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleBankApplication.class, args);
    }

}
