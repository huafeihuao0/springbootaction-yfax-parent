package com.yfax.webapi.ytt.dao;

import java.util.List;

import com.yfax.webapi.ytt.vo.BalanceHisVo;

public interface BalanceHisDao {
	public boolean insertBalanceHis(BalanceHisVo balanceHisVo) throws Exception;
	public List<BalanceHisVo> selectBalanceHisByPhoneNum(String phoneNum);
}
