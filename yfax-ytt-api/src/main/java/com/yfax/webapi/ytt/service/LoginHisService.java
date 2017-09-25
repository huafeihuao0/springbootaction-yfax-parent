package com.yfax.webapi.ytt.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yfax.webapi.ytt.dao.LoginHisDao;
import com.yfax.webapi.ytt.vo.LoginHisVo;

/**
 * 记录用户登录历史
 * @author Minbo
 */
@Service
public class LoginHisService{
	
	protected static Logger logger = LoggerFactory.getLogger(LoginHisService.class);
	
	@Autowired
	private LoginHisDao dao;
	
	public LoginHisVo selectLoginHisDate(String phoneNum) {
		return this.dao.selectLoginHisDate(phoneNum);
	}
	
	public boolean addLoginHis(LoginHisVo loginHisVo){
		try {
			return this.dao.insertLoginHis(loginHisVo);
		} catch (Exception e) {
			logger.error("记录用户登录历史异常：" + e.getMessage(), e);
			return false;
		}
	}
	
	public boolean modifyLoginHis(LoginHisVo loginHisVo){
		try {
			return this.dao.updateLoginHis(loginHisVo);
		} catch (Exception e) {
			logger.error("更新用户登录历史异常：" + e.getMessage(), e);
			return false;
		}
	}

}
