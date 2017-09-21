package com.yfax.webapi.ytt.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yfax.webapi.ytt.dao.UserSmsDao;
import com.yfax.webapi.ytt.vo.UserSmsVo;

/**
 * 记录短信验证码发送历史
 * @author Minbo
 */
@Service
public class UserSmsService{
	
	protected static Logger logger = LoggerFactory.getLogger(UserSmsService.class);
	
	@Autowired
	private UserSmsDao dao;
	
	public boolean addUserSms(UserSmsVo userSms){
		try {
			return this.dao.insertUserSms(userSms);
		} catch (Exception e) {
			logger.error("记录短信验证码发送历史异常：" + e.getMessage(), e);
			return false;
		}
	}

}
