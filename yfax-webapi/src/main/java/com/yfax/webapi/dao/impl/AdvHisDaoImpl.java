package com.yfax.webapi.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.webapi.dao.AdvHisDao;
import com.yfax.webapi.vo.AdvHisVo;

@Component
public class AdvHisDaoImpl implements AdvHisDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;;
	
	@Override
	@Transactional
	public boolean insertAdvHis(AdvHisVo advHisVo) throws Exception {
		int i = this.sqlSessionTemplate.insert("insertAdvHis", advHisVo);
		return i > 0 ? true : false;
	}

}
