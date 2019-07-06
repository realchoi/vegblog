package com.realchoi.vegblogboot;

import com.realchoi.vegblogboot.config.WebConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({WebConfig.class})
@SpringBootApplication
public class VegblogbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(VegblogbootApplication.class, args);
    }

}
