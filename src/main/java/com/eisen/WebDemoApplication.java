package com.eisen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class WebDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebDemoApplication.class, args);
    }
}


/**
 *
 * description： 一个简单的基于Spring的网页版医院预约挂号应用
 *
 * 运行步骤：
 * 1 配置idea链接sql，打开本地sql数据库
 * 2 用intellij idea 打开本工程，运行本WebDemoApplication主程序
 * 3 打开浏览器访问：http://localhost:8080/home
 * */