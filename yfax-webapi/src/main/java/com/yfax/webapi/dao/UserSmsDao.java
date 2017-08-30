package com.yfax.webapi.dao;

import com.yfax.webapi.cfdb.vo.UserSmsVo;

public interface UserSmsDao {
	public boolean insertUserSms(UserSmsVo userSms) throws Exception;
}
