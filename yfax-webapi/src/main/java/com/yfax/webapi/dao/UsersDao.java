package com.yfax.webapi.dao;

import com.yfax.webapi.vo.UsersVo;

public interface UsersDao {
	public UsersVo selectUsersByPhoneId(String phoneId);
	public boolean insertUser(UsersVo usersVo) throws Exception;
}
