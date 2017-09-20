package com.yfax.webapi.qmtt.dao.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yfax.webapi.qmtt.dao.AdvDetailDao;
import com.yfax.webapi.qmtt.vo.AdvDetailVo;


@Component
public class AdvDetailDaoImpl implements AdvDetailDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;;
	
	@Override
	public List<AdvDetailVo> selectAdvDetail(String advFkid) {
		return this.sqlSessionTemplate.selectList("selectAdvDetail", advFkid);
	}

}
