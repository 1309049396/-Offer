package com.xiehang.controller;

import com.xiehang.Service.BasicService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com")
public class BasicController {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(BasicController.class);
        BasicService basicService= context.getBean(BasicService.class);
        basicService.testSaveUser();
    }

}
