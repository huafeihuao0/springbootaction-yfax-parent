package com.yfax.webapi.qmtt.dao;

import java.util.List;

import com.yfax.webapi.qmtt.vo.AwardHisVo;

public interface AwardHisDao {
	public boolean insertAwardHis(AwardHisVo loginHis) throws Exception;
	public List<AwardHisVo> selectAwardHisByPhoneNum(String phoneNum);
}
