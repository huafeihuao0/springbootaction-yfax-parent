package com.yfax.webapi.dao;

import java.util.List;

import com.yfax.webapi.cfdb.vo.WithdrawHisVo;

public interface WithdrawHisDao {
	public List<WithdrawHisVo> selectWithdrawHis(String phoneId);
	public boolean insertWithdrawHis(WithdrawHisVo withdrawHisVo) throws Exception;
}
