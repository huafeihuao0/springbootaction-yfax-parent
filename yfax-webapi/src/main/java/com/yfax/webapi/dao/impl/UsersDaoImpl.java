package com.yfax.webapi.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.webapi.cfdb.vo.UsersVo;
import com.yfax.webapi.dao.UsersDao;

@Component
public class UsersDaoImpl implements UsersDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	@Transactional
	public UsersVo selectUsersByPhoneId(String phoneId) {
		return this.sqlSessionTemplate.selectOne("selectUsersByPhoneId", phoneId);
	}

	@Override
	@Transactional
	public boolean insert(UsersVo usersVo) throws Exception{
		int i = this.sqlSessionTemplate.insert("insert", usersVo);
		return i > 0 ? true : false;
	}

	@Override
	@Transactional
	public boolean update(UsersVo usersVo) throws Exception {
		int i = this.sqlSessionTemplate.update("update", usersVo);
		return i > 0 ? true : false;
	}

	@Override
	public String selectUsersTodayIncome(UsersVo usersVo) {
		return this.sqlSessionTemplate.selectOne("selectUsersTodayIncome", usersVo);
	}
}
