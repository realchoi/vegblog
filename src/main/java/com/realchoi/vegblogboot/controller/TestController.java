package com.realchoi.vegblogboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    /*private final WebConfig webconfig;

    @Autowired
    public TestController(WebConfig webconfig) {
        this.webconfig = webconfig;
    }*/

    @GetMapping("hello")
    public String hello() {
        return "Hello SpringBoot!";
    }

    /*@GetMapping("config")
    public String getConfig() {
        return webconfig.toString();
    }*/
}
