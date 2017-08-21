package com.yfax.webapi.qmtt.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.webapi.qmtt.dao.AppUserDao;
import com.yfax.webapi.qmtt.vo.AppUserVo;

/**
 * 用户登录服务
 * @author Minbo
 */
@Service
public class AppUserService {
	
	protected static Logger logger = LoggerFactory.getLogger(AppUserService.class);
	
	@Autowired
	private AppUserDao dao;
	
	public AppUserVo selectByPhoneNum(String phoneNum) {
		return this.dao.selectByPhoneNum(phoneNum);
	}
	
	public AppUserVo selectByPhoneNumAndPwd(Map<String, Object> params) {
		return this.dao.selectByPhoneNumAndPwd(params);
	}
	
	public List<AppUserVo> selectByRank() {
		return this.dao.selectByRank();
	}
	       
	@Transactional
	public boolean doLoginOut(String phoneNum) {
		return this.dao.deleteTokenByPhoneNum(phoneNum);
	}
	
	@Transactional
	public boolean addUser(AppUserVo appUserVo) {
		return this.dao.insertUser(appUserVo);
	}

	@Transactional
	public boolean modifyUser(AppUserVo appUserVo) {
		return this.dao.updateUser(appUserVo);
	}
}
