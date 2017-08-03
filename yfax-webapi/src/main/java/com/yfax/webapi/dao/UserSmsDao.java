package com.yfax.webapi.dao;

import com.yfax.webapi.vo.UserSmsVo;

public interface UserSmsDao {
	public boolean insertUserSms(UserSmsVo userSms) throws Exception;
}
