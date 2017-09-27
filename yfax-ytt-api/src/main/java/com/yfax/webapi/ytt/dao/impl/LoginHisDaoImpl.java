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
	public boolean insertLoginHis(LoginHisVo loginHis) throws Exception {
		int i = this.sqlSessionTemplate.insert("insertLoginHis", loginHis);
		return i > 0 ? true : false;
	}

	@Override
	public LoginHisVo selectLoginHisDate(String phoneNum) {
		return this.sqlSessionTemplate.selectOne("selectLoginHisDate", phoneNum);
	}

	@Override
	public boolean updateLoginHis(LoginHisVo loginHis) throws Exception {
		int i = this.sqlSessionTemplate.insert("updateLoginHis", loginHis);
		return i > 0 ? true : false;
	}

}
