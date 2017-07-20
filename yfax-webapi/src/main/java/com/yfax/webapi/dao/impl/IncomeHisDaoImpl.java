package com.yfax.webapi.dao.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yfax.webapi.dao.IncomeHisDao;
import com.yfax.webapi.vo.IncomeHisVo;

@Component
public class IncomeHisDaoImpl implements IncomeHisDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;;
	
	@Override
	public List<IncomeHisVo> selectIncomeHis(String phoneId) {
		return this.sqlSessionTemplate.selectList("selectIncomeHis", phoneId);
	}

}
