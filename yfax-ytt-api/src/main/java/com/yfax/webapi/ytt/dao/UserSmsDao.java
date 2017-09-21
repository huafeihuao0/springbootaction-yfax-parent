package com.yfax.webapi.ytt.dao;

import com.yfax.webapi.ytt.vo.UserSmsVo;

public interface UserSmsDao {
	public boolean insertUserSms(UserSmsVo userSms) throws Exception;
}
