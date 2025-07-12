package com.qr.transfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.qr.transfer"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}