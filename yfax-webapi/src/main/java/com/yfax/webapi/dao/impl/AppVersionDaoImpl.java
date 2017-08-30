package com.yfax.webapi.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yfax.webapi.cfdb.vo.AppVersionVo;
import com.yfax.webapi.dao.AppVersionDao;

@Component
public class AppVersionDaoImpl implements AppVersionDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public AppVersionVo selectAppVersion() {
		return this.sqlSessionTemplate.selectOne("selectAppVersion");
	}
}
