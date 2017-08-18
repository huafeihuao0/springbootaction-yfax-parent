package com.yfax.webapi.qmtt.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yfax.webapi.qmtt.dao.LoginHisDao;
import com.yfax.webapi.qmtt.vo.LoginHisVo;

/**
 * 记录用户登录历史
 * @author Minbo
 */
@Service
public class LoginHisService{
	
	protected static Logger logger = LoggerFactory.getLogger(LoginHisService.class);
	
	@Autowired
	private LoginHisDao dao;
	
	public boolean addLoginHis(LoginHisVo loginHisVo){
		try {
			return this.dao.insertLoginHis(loginHisVo);
		} catch (Exception e) {
			logger.error("记录用户登录历史异常：" + e.getMessage(), e);
			return false;
		}
	}

}
