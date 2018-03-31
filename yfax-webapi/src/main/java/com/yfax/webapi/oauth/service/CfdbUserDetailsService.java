package com.yfax.webapi.oauth.service;

import com.yfax.webapi.GlobalUtils;
import com.yfax.webapi.cfdb.vo.UsersVo;
import com.yfax.webapi.dao.UsersDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

/***
 *  【用户详情服务】
 * */
@Component("userDetailsService")
public class CfdbUserDetailsService
        implements UserDetailsService   //实现用户详情接口
{
    protected static Logger logger = LoggerFactory.getLogger(CfdbUserDetailsService.class);

    @Autowired
    private UsersDao usersDao;

    /***
     *  根据用户名加载用户
     * */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String phone)
    {
        String phoneId = phone.toLowerCase();
        //只要判断是否存在用户记录即可
        UsersVo userFromDatabase = this.usersDao.selectUsersByPhoneId(phoneId);
        if (userFromDatabase == null)
        {
            logger.info("UsersVo " + phoneId + " was not exist");
            throw new UsernameNotFoundException("UsersVo " + phoneId + " was not found in the database");
        }
        //获取用户的所有权限并且SpringSecurity需要的集合
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));  //普通用户权限
        //返回一个SpringSecurity需要的用户对象
        return new User(
                userFromDatabase.getPhoneId(),
                GlobalUtils.CFDB_PWD,
                grantedAuthorities
        );
    }
}

