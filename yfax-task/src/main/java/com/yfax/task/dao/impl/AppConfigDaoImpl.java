package com.yfax.task.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yfax.task.dao.AppConfigDao;
import com.yfax.task.vo.AppConfigVo;


@Component
public class AppConfigDaoImpl implements AppConfigDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;;
	
	@Override
	public AppConfigVo selectAppConfig() {
		return this.sqlSessionTemplate.selectOne("selectAppConfig");
	}

}
