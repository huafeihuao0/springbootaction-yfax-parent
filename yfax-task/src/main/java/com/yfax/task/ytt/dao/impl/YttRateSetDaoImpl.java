package com.yfax.task.ytt.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yfax.task.ytt.dao.YttRateSetDao;
import com.yfax.task.ytt.vo.YttRateSetVo;


@Component
public class YttRateSetDaoImpl implements YttRateSetDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;;
	
	@Override
	public YttRateSetVo selectRateSet() {
		return this.sqlSessionTemplate.selectOne("selectYttRateSet");
	}

}
