package com.yfax.task.ytt.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yfax.task.ytt.dao.YttAppConfigDao;
import com.yfax.task.ytt.vo.YttAppConfigVo;


@Component
public class YttAppConfigDaoImpl implements YttAppConfigDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;;
	
	@Override
	public YttAppConfigVo selectAppConfig() {
		return this.sqlSessionTemplate.selectOne("selectAppConfig");
	}

}
