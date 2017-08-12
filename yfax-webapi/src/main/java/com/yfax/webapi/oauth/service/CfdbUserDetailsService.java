package com.yfax.webapi.oauth.service;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.webapi.GlobalUtils;
import com.yfax.webapi.cfdb.vo.UsersVo;
import com.yfax.webapi.dao.UsersDao;

@Component("userDetailsService")
public class CfdbUserDetailsService implements UserDetailsService {
	
	protected static Logger logger = LoggerFactory.getLogger(CfdbUserDetailsService.class);
	
    @Autowired
	private UsersDao dao;
	
    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        String phoneId = login.toLowerCase();
        //只要判断是否存在用户记录即可
        UsersVo userFromDatabase = this.dao.selectUsersByPhoneId(phoneId);
        if (userFromDatabase == null) {
        		logger.info("UsersVo " + phoneId + " was not exist");
            throw new UsernameNotFoundException("UsersVo " + phoneId + " was not found in the database");
        }
        //获取用户的所有权限并且SpringSecurity需要的集合
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        grantedAuthorities.add(grantedAuthority);
        //返回一个SpringSecurity需要的用户对象
        return new org.springframework.security.core.userdetails.User(
                userFromDatabase.getPhoneId(),
                GlobalUtils.CFDB_PWD,
                grantedAuthorities);
    }
}

