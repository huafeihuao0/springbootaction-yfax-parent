package com.yfax.webapi.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yfax.webapi.cfdb.vo.ApkUrlVo;
import com.yfax.webapi.dao.ApkUrlDao;

@Component
public class ApkUrlDaoImpl implements ApkUrlDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public ApkUrlVo selectApkUrl() {
		return this.sqlSessionTemplate.selectOne("selectApkUrl");
	}
}
