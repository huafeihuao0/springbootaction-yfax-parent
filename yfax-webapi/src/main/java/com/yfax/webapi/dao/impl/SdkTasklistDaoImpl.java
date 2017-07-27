package com.yfax.webapi.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.webapi.dao.SdkTasklistDao;
import com.yfax.webapi.vo.SdkTasklistVo;

@Component
public class SdkTasklistDaoImpl implements SdkTasklistDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;;
	
	@Override
	@Transactional
	public boolean insertSdkTasklist(SdkTasklistVo sdkTasklistVo) throws Exception {
		int i = this.sqlSessionTemplate.insert("insertSdkTasklist", sdkTasklistVo);
		return i > 0 ? true : false;
	}

	@Override
	public SdkTasklistVo selectSdkTasklistByAdid(String adid) {
		return this.sqlSessionTemplate.selectOne("selectSdkTasklistByAdid", adid);
	}
}
