package com.yfax.webapi.qmtt.dao;

import com.yfax.webapi.qmtt.vo.ShareUserHisVo;

public interface ShareUserHisDao {
	public Long selectCountByPhoneNum(String phoneNum);
	public boolean insertShareUserHis(ShareUserHisVo shareUserHisVo) throws Exception;
	public ShareUserHisVo selectShareUserByStudentPhoneNum(String phoneNum);
}
