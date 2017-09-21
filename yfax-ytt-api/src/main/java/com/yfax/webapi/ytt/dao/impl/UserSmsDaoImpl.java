package com.yfax.webapi.ytt.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.webapi.ytt.dao.UserSmsDao;
import com.yfax.webapi.ytt.vo.UserSmsVo;


@Component
public class UserSmsDaoImpl implements UserSmsDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;;
	

	@Override
	@Transactional
	public boolean insertUserSms(UserSmsVo userSms) throws Exception {
		int i = this.sqlSessionTemplate.insert("insertUserSms", userSms);
		return i > 0 ? true : false;
	}

}
