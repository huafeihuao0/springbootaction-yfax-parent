package com.yfax.task.qmtt.dao;

import java.util.List;

import com.yfax.task.qmtt.vo.BalanceHisVo;

public interface BalanceHisDao {
	public boolean insertBalanceHis(BalanceHisVo loginHis) throws Exception;
	public List<BalanceHisVo> selectBalanceHisByPhoneNum(String phoneNum);
}
