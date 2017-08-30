package com.yfax.webapi.wdkz.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yfax.webapi.wdkz.dao.WdkzAppConfigDao;
import com.yfax.webapi.wdkz.vo.WdkzAppConfigVo;


@Component
public class WdkzAppConfigDaoImpl implements WdkzAppConfigDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;;
	
	@Override
	public WdkzAppConfigVo selectAppConfigByVersion(String version) {
		return this.sqlSessionTemplate.selectOne("selectAppConfigByVersion", version);
	}

}
