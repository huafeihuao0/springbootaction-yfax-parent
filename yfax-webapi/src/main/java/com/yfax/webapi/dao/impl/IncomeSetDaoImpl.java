package com.yfax.webapi.dao.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yfax.webapi.cfdb.vo.IncomeSetVo;
import com.yfax.webapi.dao.IncomeSetDao;

@Component
public class IncomeSetDaoImpl implements IncomeSetDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public List<IncomeSetVo> selectIncomeSetList() {
		return this.sqlSessionTemplate.selectList("selectIncomeSetList");
	};
}
