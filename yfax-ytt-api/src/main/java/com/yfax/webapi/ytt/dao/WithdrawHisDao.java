package com.yfax.webapi.ytt.dao;

import java.util.List;

import com.yfax.webapi.ytt.vo.WithdrawHisVo;

public interface WithdrawHisDao {
	public List<WithdrawHisVo> selectWithdrawHis(String phoneId);
	public boolean insertWithdrawHis(WithdrawHisVo withdrawHisVo) throws Exception;
}
