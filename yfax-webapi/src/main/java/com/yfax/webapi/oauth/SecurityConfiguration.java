package com.yfax.webapi.oauth;

import com.yfax.webapi.GlobalUtils;
import com.yfax.webapi.oauth.service.CfdbUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

/***
 *  【开启安全配置】
 * */
@Configuration
@EnableWebSecurity  //启动web安全配置
public class SecurityConfiguration
        extends WebSecurityConfigurerAdapter//继承安全配置器适配器
{
    //自定义UserDetailsService注入
    @Autowired
    private CfdbUserDetailsService userDetailsService;


    /***
     *  配置全局认证策略
     * */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authManagerBuilder) throws Exception
    {
        //设置UserDetailsService以及密码规则
        authManagerBuilder.userDetailsService(userDetailsService);
    }

    /***
     *  配置具体的安全策略
     *
     排除/hello路径拦截
     版本升级
     广告平台回调不做拦截
     faq页不做拦截
     登录不做拦截（因为同时要创建新用户）
     * */
    @Override
    public void configure(WebSecurity webSecurity) throws Exception
    {
        webSecurity.ignoring()
                   .antMatchers("/hello"
                           , GlobalUtils.PROJECT_CFDB + "/queryUpgradeByVersion"
                           , GlobalUtils.PROJECT_CFDB + "/sendAdvInfo"
                           , GlobalUtils.PROJECT_CFDB + "/sendAdvInfoYm"
                           , GlobalUtils.PROJECT_CFDB + "/faq"
                           , GlobalUtils.PROJECT_CFDB + "/doLogin");
    }

    /***
     *  认证映射bean
     * */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        return super.authenticationManagerBean();
    }

    /***
     *  开启全局方法拦截
     * */
    @EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
    public static class GlobalSecurityConfiguration
            extends GlobalMethodSecurityConfiguration  //全局方法安全配置
    {
        @Override
        protected MethodSecurityExpressionHandler createExpressionHandler()
        {
            return new OAuth2MethodSecurityExpressionHandler();
        }
    }
}