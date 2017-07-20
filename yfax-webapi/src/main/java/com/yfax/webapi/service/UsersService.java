package com.yfax.webapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yfax.webapi.dao.UsersDao;
import com.yfax.webapi.utils.DateUtil;
import com.yfax.webapi.vo.UsersVo;

/**
 * 用户管理
 * @author Minbo
 */
@Service
public class UsersService{
	
	protected static Logger logger = LoggerFactory.getLogger(UsersService.class);
	
	@Autowired
	private UsersDao dao;
	
	public UsersVo selectUsersByPhoneId(String phoneId){
		return this.dao.selectUsersByPhoneId(phoneId);
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
		String cTime = DateUtil.getCurrentLongDateTime();
		usersVo.setCreateDate(cTime);
		usersVo.setUpdateDate(cTime);
		try {
			return this.dao.insertUser(usersVo);
		} catch (Exception e) {
			logger.error("新增手机IM用户异常：" + e.getMessage(), e);
			return false;
		}
	}

}
