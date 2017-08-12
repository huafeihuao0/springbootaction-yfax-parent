package com.yfax.webapi.dao.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yfax.webapi.cfdb.vo.AppBannerVo;
import com.yfax.webapi.dao.AppBannerDao;

@Component
public class AppBannerDaoImpl implements AppBannerDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public List<AppBannerVo> selectAppBannerList() {
		return this.sqlSessionTemplate.selectList("selectAppBanner");
	};
}
