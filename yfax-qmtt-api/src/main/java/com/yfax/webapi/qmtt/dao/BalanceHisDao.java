package com.yfax.webapi.qmtt.dao;

import java.util.List;

import com.yfax.webapi.qmtt.vo.BalanceHisVo;

public interface BalanceHisDao {
	public boolean insertBalanceHis(BalanceHisVo balanceHisVo) throws Exception;
	public List<BalanceHisVo> selectBalanceHisByPhoneNum(String phoneNum);
}
