package com.yfax.webapi.dao.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yfax.webapi.dao.SdkChannelConfigDao;
import com.yfax.webapi.vo.SdkChannelConfigVo;

@Component
public class SdkChannelConfigDaoImpl implements SdkChannelConfigDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public List<SdkChannelConfigVo> selectSdkChannelConfigList() {
		return this.sqlSessionTemplate.selectList("selectSdkChannelConfig");
	}
}
