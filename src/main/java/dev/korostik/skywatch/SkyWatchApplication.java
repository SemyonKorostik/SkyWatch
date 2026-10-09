package dev.korostik.skywatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class SkyWatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkyWatchApplication.class, args);
    }

}
