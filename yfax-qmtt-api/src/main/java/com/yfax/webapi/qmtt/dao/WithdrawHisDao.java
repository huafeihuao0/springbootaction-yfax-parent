package com.yfax.webapi.qmtt.dao;

import java.util.List;

import com.yfax.webapi.qmtt.vo.WithdrawHisVo;

public interface WithdrawHisDao {
	public List<WithdrawHisVo> selectWithdrawHis(String phoneId);
	public boolean insertWithdrawHis(WithdrawHisVo withdrawHisVo) throws Exception;
}
