package com.yfax.webapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Minbo.He
 */
@SpringBootApplication
@EnableTransactionManagement
public class Application{
	
	// 服务启动入口
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
