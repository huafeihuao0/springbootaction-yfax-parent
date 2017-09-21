package com.yfax.webapi.ytt.dao;

import com.yfax.webapi.ytt.vo.ShareUserHisVo;

public interface ShareUserHisDao {
	public Long selectCountByPhoneNum(String phoneNum);
	public boolean insertShareUserHis(ShareUserHisVo shareUserHisVo) throws Exception;
	public ShareUserHisVo selectShareUserByStudentPhoneNum(String phoneNum);
}
