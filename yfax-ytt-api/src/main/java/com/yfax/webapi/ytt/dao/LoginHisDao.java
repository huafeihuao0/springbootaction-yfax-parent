package com.yfax.webapi.ytt.dao;

import com.yfax.webapi.ytt.vo.LoginHisVo;

public interface LoginHisDao {
	public LoginHisVo selectLoginHisDate(String phoneNum);
	public boolean insertLoginHis(LoginHisVo loginHis) throws Exception;
	public boolean updateLoginHis(LoginHisVo loginHis) throws Exception;
}
