package com.yfax.webapi.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.webapi.cfdb.vo.UserTasklistHisVo;
import com.yfax.webapi.dao.UserTasklistHisDao;

@Component
public class UserTasklistHisDaoImpl implements UserTasklistHisDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;;
	
	@Override
	@Transactional
	public boolean insertUserTasklistHis(UserTasklistHisVo advHisVo) throws Exception {
		int i = this.sqlSessionTemplate.insert("insertUserTasklistHis", advHisVo);
		return i > 0 ? true : false;
	}

}
