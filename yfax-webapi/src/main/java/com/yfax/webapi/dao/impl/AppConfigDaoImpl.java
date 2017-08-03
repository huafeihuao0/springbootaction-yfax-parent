package com.yfax.webapi.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yfax.webapi.dao.AppConfigDao;
import com.yfax.webapi.vo.AppConfigVo;

@Component
public class AppConfigDaoImpl implements AppConfigDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public AppConfigVo selectAppConfig() {
		return this.sqlSessionTemplate.selectOne("selectAppConfig");
	}
}
