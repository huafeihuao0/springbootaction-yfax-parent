package com.yfax.webapi.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yfax.webapi.cfdb.vo.UsersVo;
import com.yfax.webapi.dao.UsersDao;
import com.yfax.utils.DateUtil;

/**
 * 用户管理
 * @author Minbo
 */
@Service
public class UsersService{
	
	protected static Logger logger = LoggerFactory.getLogger(UsersService.class);
	
	@Autowired
	private UsersDao usersDao;
	
	public UsersVo selectUsersByPhoneId(String phoneId){
		return this.usersDao.selectUsersByPhoneId(phoneId);
	}
	
	/**
	 * 获取用户统计的今日收入
	 * @param usersVo
	 * @return
	 */
	public String selectUsersTodayIncome(UsersVo usersVo){
		return this.usersDao.selectUsersTodayIncome(usersVo);
	}
	
	/**
	 * 新增手机IM用户
	 * @param usersVo
	 * @return
	 */
	public boolean addUser(String phoneId) {
		UsersVo usersVo = new UsersVo();
		usersVo.setPhoneId(phoneId);
		usersVo.setBalance("0");
		usersVo.setTotalIncome("0");
		String cTime = DateUtil.getCurrentLongDateTime();
		usersVo.setCreateDate(cTime);
		try {
			return this.usersDao.insert(usersVo);
		} catch (Exception e) {
			logger.error("新增手机IM用户异常：" + e.getMessage(), e);
			return false;
		}
	}

}
