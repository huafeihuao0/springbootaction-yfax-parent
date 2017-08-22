package com.yfax.webapi.qmtt.dao;

import com.yfax.webapi.qmtt.vo.AppShareCodeVo;

public interface AppShareCodeDao {
	public boolean insertAppShareCode(AppShareCodeVo appShareCodeVo) throws Exception;
	public AppShareCodeVo selectAppShareCodeByPhoneNum(String phoneNum);
}
