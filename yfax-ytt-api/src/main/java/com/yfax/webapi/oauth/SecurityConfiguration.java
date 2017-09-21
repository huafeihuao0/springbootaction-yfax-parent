package com.yfax.webapi.oauth;

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

import com.yfax.webapi.GlobalUtils;
import com.yfax.webapi.oauth.service.CfdbUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
    //自定义UserDetailsService注入
    @Autowired
    private CfdbUserDetailsService userDetailsService;
    
    //配置匹配用户时密码规则
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return null;
//    }
    
    //配置全局设置
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //设置UserDetailsService以及密码规则
        auth.userDetailsService(userDetailsService);
    }
    
    	//排除/hello路径拦截
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/hello"
        		, GlobalUtils.PROJECT_YTT + "/doRegister"
        		, GlobalUtils.PROJECT_YTT + "/queryInitConfig"
        		, GlobalUtils.PROJECT_YTT + "/faq"
        		, GlobalUtils.PROJECT_YTT + "/invite"
        		, GlobalUtils.PROJECT_YTT + "/register"
        		, GlobalUtils.PROJECT_YTT + "/download"
        		, GlobalUtils.PROJECT_YTT + "/doLogin"
        		, GlobalUtils.PROJECT_YTT + "/doSms"
        		, GlobalUtils.PROJECT_YTT + "/doDownloadUrl"
        		, GlobalUtils.PROJECT_YTT + "/doResetPwd"
        		, GlobalUtils.PROJECT_YTT + "/doRedirectUrl"
        		, GlobalUtils.PROJECT_YTT + "/queryRank"
        		, GlobalUtils.PROJECT_YTT + "/queryRankGold"
        		, GlobalUtils.PROJECT_YTT + "/queryAdvList");
    }
    
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    //开启全局方法拦截
    @EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
    public static class GlobalSecurityConfiguration extends GlobalMethodSecurityConfiguration {
        @Override
        protected MethodSecurityExpressionHandler createExpressionHandler() {
            return new OAuth2MethodSecurityExpressionHandler();
        }
    }
}