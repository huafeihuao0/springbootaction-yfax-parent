package com.yfax.task.qmtt.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yfax.task.qmtt.dao.RateSetDao;
import com.yfax.task.qmtt.vo.RateSetVo;


@Component
public class RateSetDaoImpl implements RateSetDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;;
	
	@Override
	public RateSetVo selectRateSet() {
		return this.sqlSessionTemplate.selectOne("selectRateSet");
	}

}
