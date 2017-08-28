package com.yfax.webapi.qmtt.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.yfax.webapi.qmtt.dao.InitConfigDao;
import com.yfax.webapi.qmtt.vo.InitConfigVo;


@Component
public class InitConfigDaoImpl implements InitConfigDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;;
	
	@Override
	public InitConfigVo selectInitConfig() {
		return this.sqlSessionTemplate.selectOne("selectInitConfig");
	}

}
