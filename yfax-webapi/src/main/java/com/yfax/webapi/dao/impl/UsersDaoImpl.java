package com.yfax.webapi.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.webapi.dao.UsersDao;
import com.yfax.webapi.vo.UsersVo;

@Component
public class UsersDaoImpl implements UsersDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public UsersVo selectUsersByPhoneId(String phoneId) {
		return this.sqlSessionTemplate.selectOne("selectUsersByPhoneId", phoneId);
	}

	@Override
	@Transactional
	public boolean insertUser(UsersVo usersVo) throws Exception{
		int i = this.sqlSessionTemplate.insert("insertUser", usersVo);
		return i > 0 ? true : false;
	}
}
