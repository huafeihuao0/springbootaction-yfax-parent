package com.yfax.webapi.ytt.dao.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yfax.webapi.ytt.dao.AdvListDao;
import com.yfax.webapi.ytt.vo.AdvListVo;


@Component
public class AdvListDaoImpl implements AdvListDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;;
	
	@Override
	public List<AdvListVo> selectAdvList() {
		return this.sqlSessionTemplate.selectList("selectAdvList");
	}

}
