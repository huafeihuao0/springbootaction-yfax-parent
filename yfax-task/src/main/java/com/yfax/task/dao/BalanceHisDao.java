package com.yfax.task.dao;

import java.util.List;

import com.yfax.task.vo.BalanceHisVo;

public interface BalanceHisDao {
	public boolean insertBalanceHis(BalanceHisVo loginHis) throws Exception;
	public List<BalanceHisVo> selectBalanceHisByPhoneNum(String phoneNum);
}
