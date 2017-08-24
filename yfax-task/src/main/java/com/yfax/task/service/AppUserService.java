package com.yfax.task.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.task.dao.AppUserDao;
import com.yfax.task.vo.AppUserVo;

/**
 * 用户管理服务
 * @author Minbo
 */
@Service
public class AppUserService {
	
	protected static Logger logger = LoggerFactory.getLogger(AppUserService.class);
	
	@Autowired
	private AppUserDao appUserDao;
	
	public AppUserVo selectByPhoneNum(String phoneNum) {
		return this.appUserDao.selectByPhoneNum(phoneNum);
	}
	
	public AppUserVo selectByPhoneNumAndPwd(Map<String, Object> params) {
		return this.appUserDao.selectByPhoneNumAndPwd(params);
	}
	
	public List<AppUserVo> selectByRank() {
		return this.appUserDao.selectByRank();
	}
	
	public Long selectByRankSum() {
		return this.appUserDao.selectByRankSum();
	}
	       
	@Transactional
	public boolean doLoginOut(String phoneNum) {
		return this.appUserDao.deleteTokenByPhoneNum(phoneNum);
	}
	
	@Transactional
	public boolean addUser(AppUserVo appUserVo) {
		return this.appUserDao.insertUser(appUserVo);
	}

	@Transactional
	public boolean modifyUser(AppUserVo appUserVo) {
		return this.appUserDao.updateUser(appUserVo);
	}
	
	public List<AppUserVo> selectByPhoneNumGoldLimit() {
		return this.appUserDao.selectByPhoneNumGoldLimit();
	}
	
	public List<AppUserVo> selectAllUser(){
		return this.appUserDao.selectAllUser();
	}
}
