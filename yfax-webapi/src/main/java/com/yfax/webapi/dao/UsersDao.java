package com.yfax.webapi.dao;

import com.yfax.webapi.vo.UsersVo;

public interface UsersDao {
	public UsersVo selectUsersByPhoneId(String phoneId);
	public boolean insertUser(UsersVo usersVo) throws Exception;
	public boolean updateUser(UsersVo usersVo) throws Exception;
	public String selectUsersTodayIncome(UsersVo usersVo);
}
