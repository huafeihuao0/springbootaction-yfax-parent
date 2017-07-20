package com.yfax.webapi.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.yfax.webapi.web.listener.MyApplicationEnvironmentPreparedEventListener;
import com.yfax.webapi.web.listener.MyApplicationFailedEventListener;
import com.yfax.webapi.web.listener.MyApplicationPreparedEventListener;
import com.yfax.webapi.web.listener.MyApplicationReadyEventListener;
import com.yfax.webapi.web.listener.MyApplicationStartedEventListener;

/**
 * 参考网址： 
 * 1. https://www.tianmaying.com/tutorial/spring-mvc-quickstart 
 * 2. https://www.tianmaying.com/tutorial/spring-boot-overview
 * 
 * @author Minbo.He
 */
@SpringBootApplication
public class MyApplication {
//	// 启动入口
//	public static void main(String[] args) {
//		
//		//spring boot实战：在启动过程中增加事件监听机制
////		SpringApplication app = new SpringApplication(Application.class);
////		app.addListeners(new MyApplicationStartedEventListener());
////		app.addListeners(new MyApplicationEnvironmentPreparedEventListener());
////		app.addListeners(new MyApplicationPreparedEventListener());
////		app.addListeners(new MyApplicationReadyEventListener());
////		app.addListeners(new MyApplicationFailedEventListener());
////		app.run(args);
//
//		SpringApplication.run(MyApplication.class, args);
//	}
}
