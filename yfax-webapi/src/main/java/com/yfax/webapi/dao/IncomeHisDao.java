package com.yfax.webapi.dao;

import java.util.List;

import com.yfax.webapi.cfdb.vo.IncomeHisVo;

public interface IncomeHisDao {
	public List<IncomeHisVo> selectIncomeHis(String phoneId);
	public boolean insertIncomeHis(IncomeHisVo incomeHis) throws Exception;
	public IncomeHisVo selectIncomeHisByCondition(IncomeHisVo incomeHisVo);
}
