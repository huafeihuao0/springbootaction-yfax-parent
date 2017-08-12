package com.yfax.webapi.dao;

import java.util.Map;

import com.yfax.webapi.cfdb.vo.UsersVo;

public interface UsersDao {
	public UsersVo selectUsersByPhoneId(String phoneId);
	public boolean insert(UsersVo usersVo) throws Exception;
	public boolean update(UsersVo usersVo) throws Exception;
	public String selectUsersTodayIncome(UsersVo usersVo);
}
