package com.yfax.webapi;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;


/***
 *  【springboot servlet 初始化器】
 * */
public class ServletInitializer
        extends SpringBootServletInitializer//SB servlet初始化器
{

    /***
     *  加载应用配置
     * */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder springAppBuilder)
    {
        return springAppBuilder.sources(Application.class);
    }

}