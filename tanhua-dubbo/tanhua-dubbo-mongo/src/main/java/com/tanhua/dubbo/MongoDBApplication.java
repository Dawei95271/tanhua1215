package com.tanhua.dubbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/19 16:41
 */
@SpringBootApplication
@EnableAsync
public class MongoDBApplication {

    public static void main(String[] args) {
        SpringApplication.run(MongoDBApplication.class, args);
    }
}
