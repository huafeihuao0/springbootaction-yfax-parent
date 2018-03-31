package com.yfax.webapi;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/***
 *  【mvc配置】
 * */
@Configuration
public class WebMVCConfig
        extends WebMvcConfigurerAdapter//继承webmvc
{
    /***
     *  添加拦截器
     * */
    @Override
    public void addInterceptors(InterceptorRegistry interceptorReg)//拦截器注册表
    {
        //定义过滤拦截的url名称，例如/api/cfdb/**，表示cfdb下所有的名称请求
        interceptorReg.addInterceptor(new HttpInterceptor())
                .addPathPatterns(GlobalUtils.PROJECT_CFDB + "/**");
    }
}
