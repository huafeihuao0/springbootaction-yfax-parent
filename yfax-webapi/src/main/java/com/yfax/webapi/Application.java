package com.yfax.webapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/***
 *  【SB应用入口和配置】
 * */
@SpringBootApplication  //SB app
@EnableTransactionManagement    // 开启自动事务管理
@EnableAsync(proxyTargetClass = true)   //开启异步支持
public class Application
{
    // 服务启动入口
    public static void main(String[] args)
    {
        //开启运行
        SpringApplication.run(Application.class, args);
    }
}
