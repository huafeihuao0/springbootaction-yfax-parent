package com.yfax.webapi.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yfax.webapi.dao.ApkUrlDao;
import com.yfax.webapi.vo.ApkUrlVo;

@Component
public class ApkUrlDaoImpl implements ApkUrlDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public ApkUrlVo selectApkUrl() {
		return this.sqlSessionTemplate.selectOne("selectApkUrl");
	}
}
