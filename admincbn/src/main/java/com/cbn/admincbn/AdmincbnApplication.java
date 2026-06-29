package com.cbn.admincbn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AdmincbnApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdmincbnApplication.class, args);
    }
}
