package com.tanhua.dubbo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/16 17:16
 */

@SpringBootApplication
@MapperScan("com.tanhua.dubbo.mapper")
public class DubboDBApplication {

    public static void main(String[] args) {

        SpringApplication.run(DubboDBApplication.class, args);
    }
}
