package com.yfax.webapi.ytt.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yfax.webapi.ytt.dao.StudentConfigDao;
import com.yfax.webapi.ytt.vo.StudentConfigVo;


@Component
public class StudentConfigDaoImpl implements StudentConfigDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;;
	
	@Override
	public StudentConfigVo selectStudentConfig(long awardCount) {
		return this.sqlSessionTemplate.selectOne("selectStudentConfig", awardCount);
	}

}
