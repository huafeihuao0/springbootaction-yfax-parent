package com.yfax.webapi.ytt.dao;

import com.yfax.webapi.ytt.vo.AppShareCodeVo;

public interface AppShareCodeDao {
	public boolean insertAppShareCode(AppShareCodeVo appShareCodeVo) throws Exception;
	public AppShareCodeVo selectAppShareCodeByPhoneNum(String phoneNum);
	public AppShareCodeVo selectAppShareCodeByShareCode(String shareCode);
}
