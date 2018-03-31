package com.yfax.webapi.oauth;

import com.yfax.webapi.GlobalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

/***
 *  oauth2配置
 * */
@Configuration
public class OAuth2Configuration
{
    /***
     *  注册一个资源服务的配置种子
     * */
    @Configuration
    @EnableResourceServer //启用资源服务器
    protected static class ResourceServerConfiguration
            extends ResourceServerConfigurerAdapter //资源服务适配器
    {

        @Autowired
        private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

        @Autowired
        private CustomLogoutSuccessHandler customLogoutSuccessHandler;

        /***
         *  配置
         * */
        @Override
        public void configure(HttpSecurity httpSecurity) throws Exception
        {
            httpSecurity
                    .exceptionHandling()
                    .authenticationEntryPoint(customAuthenticationEntryPoint)
                    .and()
                    .logout()
                    .logoutUrl("/oauth/logout")
                    .logoutSuccessHandler(customLogoutSuccessHandler)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/hello/",
                            GlobalUtils.PROJECT_CFDB + "/queryUpgradeByVersion",
                            GlobalUtils.PROJECT_CFDB + "/doLogin")
                    .permitAll()
                    .antMatchers("/secure/**",
                            GlobalUtils.PROJECT_CFDB + "/**")
                    .authenticated();
        }

    }

    /***
     *  注册认证服务配置
     * */
    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration
            extends AuthorizationServerConfigurerAdapter
            implements EnvironmentAware
    {

        private static final String ENV_OAUTH = "authentication.oauth.";
        private static final String PROP_CLIENTID = "clientid";
        private static final String PROP_SECRET = "secret";
        private static final String PROP_TOKEN_VALIDITY_SECONDS = "tokenValidityInSeconds";

        private RelaxedPropertyResolver propertyResolver;

        @Autowired
        private DataSource dataSource;

        /***
         *  注册jdbc令牌
         * */
        @Bean
        public TokenStore tokenStore()
        {
            return new JdbcTokenStore(dataSource);
        }

        /***
         *  注入认证管理器
         * */
        @Autowired
        @Qualifier("authenticationManagerBean")
        private AuthenticationManager authenticationManager;

        /***
         *  配置终端
         * */
        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints)
        throws Exception
        {
            endpoints.tokenStore(tokenStore())
                     .authenticationManager(authenticationManager);
        }

        /***
         *  配置客户端
         * */
        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception
        {
            clients.inMemory()
                   .withClient(propertyResolver.getProperty(PROP_CLIENTID))
                   .scopes("read", "write")
                   .authorities(Authorities.ROLE_ADMIN.name(), Authorities.ROLE_USER.name())
                   .authorizedGrantTypes("password", "refresh_token")
                   .secret(propertyResolver.getProperty(PROP_SECRET))
                   .accessTokenValiditySeconds(propertyResolver.getProperty(PROP_TOKEN_VALIDITY_SECONDS, Integer.class, 120));
        }

        /***
         *  设置环境
         * */
        @Override
        public void setEnvironment(Environment environment)
        {
            this.propertyResolver = new RelaxedPropertyResolver(environment, ENV_OAUTH);
        }
    }
}
