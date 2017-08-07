package com.yfax.webapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Minbo.He
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableAsync(proxyTargetClass=true)
public class Application{
	
	// 服务启动入口
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
