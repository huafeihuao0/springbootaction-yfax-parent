package com.yfax.webapi.ytt.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.webapi.ytt.dao.UserFeedbackDao;
import com.yfax.webapi.ytt.vo.UserFeedbackVo;

@Component
public class UserFeedbackDaoImpl implements UserFeedbackDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;;

	@Override
	@Transactional
	public boolean insertUserFeedback(UserFeedbackVo userFeedback) throws Exception {
		int i = this.sqlSessionTemplate.insert("insertUserFeedback", userFeedback);
		return i > 0 ? true : false;
	}

}
