package com.yfax.webapi.dao;

import java.util.List;

import com.yfax.webapi.cfdb.vo.IncomeSetVo;

public interface IncomeSetDao {
	public List<IncomeSetVo> selectIncomeSetList();
}
