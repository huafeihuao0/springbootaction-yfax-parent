package com.yfax.webapi.qmtt.dao.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.yfax.webapi.qmtt.dao.IncomeSetDao;
import com.yfax.webapi.qmtt.vo.IncomeSetVo;


@Component
public class IncomeSetDaoImpl implements IncomeSetDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;;
	
	@Override
	public List<IncomeSetVo> selectIncomeSet() {
		return this.sqlSessionTemplate.selectList("selectIncomeSet");
	}

}
