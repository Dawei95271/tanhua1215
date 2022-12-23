package com.tanhua.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/15 21:30
 */
@SpringBootApplication
@EnableCaching
public class AppServerApplication {

    public static void main(String[] args) {

        SpringApplication.run(AppServerApplication.class, args);
    }
}
