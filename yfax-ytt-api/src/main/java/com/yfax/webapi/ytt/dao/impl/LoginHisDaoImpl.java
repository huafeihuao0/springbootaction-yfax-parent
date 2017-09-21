package com.yfax.webapi.ytt.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.webapi.ytt.dao.LoginHisDao;
import com.yfax.webapi.ytt.vo.LoginHisVo;


@Component
public class LoginHisDaoImpl implements LoginHisDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;;
	
	@Override
	@Transactional
	public boolean insertLoginHis(LoginHisVo userSms) throws Exception {
		int i = this.sqlSessionTemplate.insert("insertLoginHis", userSms);
		return i > 0 ? true : false;
	}

}
