package com.yfax.task.ytt.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.task.ytt.dao.YttAppUserDao;
import com.yfax.task.ytt.vo.YttAppUserVo;

/**
 * 用户管理服务
 * @author Minbo
 */
@Service
public class YttAppUserService {
	
	protected static Logger logger = LoggerFactory.getLogger(YttAppUserService.class);
	
	@Autowired
	private YttAppUserDao dao;
	
	public YttAppUserVo selectByPhoneNum(String phoneNum) {
		return this.dao.selectByPhoneNum(phoneNum);
	}
	
	public YttAppUserVo selectByPhoneNumAndPwd(Map<String, Object> params) {
		return this.dao.selectByPhoneNumAndPwd(params);
	}
	
	public List<YttAppUserVo> selectByRank() {
		return this.dao.selectByRank();
	}
	
	public Long selectByRankSum() {
		return this.dao.selectByRankSum();
	}
	       
	@Transactional
	public boolean doLoginOut(String phoneNum) {
		return this.dao.deleteTokenByPhoneNum(phoneNum);
	}
	
	@Transactional
	public boolean addUser(YttAppUserVo appUserVo) {
		return this.dao.insertUser(appUserVo);
	}

	@Transactional
	public boolean modifyUser(YttAppUserVo appUserVo) {
		return this.dao.updateUser(appUserVo);
	}
	
	public List<YttAppUserVo> selectByPhoneNumGoldLimit() {
		return this.dao.selectByPhoneNumGoldLimit();
	}
	
	public List<YttAppUserVo> selectAllUser(){
		return this.dao.selectAllUser();
	}
}
