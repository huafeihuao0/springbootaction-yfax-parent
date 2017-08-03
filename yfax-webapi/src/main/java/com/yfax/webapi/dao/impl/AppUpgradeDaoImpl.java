package com.yfax.webapi.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yfax.webapi.dao.AppUpgradeDao;
import com.yfax.webapi.vo.AppUpgradeVo;

@Component
public class AppUpgradeDaoImpl implements AppUpgradeDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public AppUpgradeVo selectAppUpgrade() {
		return this.sqlSessionTemplate.selectOne("selectAppUpgrade");
	}
}
