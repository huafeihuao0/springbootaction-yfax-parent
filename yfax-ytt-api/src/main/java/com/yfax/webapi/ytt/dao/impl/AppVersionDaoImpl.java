package com.yfax.webapi.ytt.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yfax.webapi.ytt.dao.AppVersionDao;
import com.yfax.webapi.ytt.vo.AppVersionVo;

@Component
public class AppVersionDaoImpl implements AppVersionDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public AppVersionVo selectAppVersion() {
		return this.sqlSessionTemplate.selectOne("selectAppVersion");
	}
}
