package com.yfax.webapi.qmtt.dao;

import com.yfax.webapi.qmtt.vo.UserSmsVo;

public interface UserSmsDao {
	public boolean insertUserSms(UserSmsVo userSms) throws Exception;
}
