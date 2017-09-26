package com.yfax.task.ytt.dao;

import java.util.List;

import com.yfax.task.ytt.vo.YttBalanceHisVo;

public interface YttBalanceHisDao {
	public boolean insertBalanceHis(YttBalanceHisVo loginHis) throws Exception;
	public List<YttBalanceHisVo> selectBalanceHisByPhoneNum(String phoneNum);
}
