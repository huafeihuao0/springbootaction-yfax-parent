package com.yfax.webapi.ytt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yfax.webapi.ytt.dao.AppVersionDao;
import com.yfax.webapi.ytt.vo.AppVersionVo;

/**
 * APK渠道版本配置
 * @author Minbo
 */
@Service
public class AppVersionService{
	
	@Autowired
	private AppVersionDao dao;
	
	public AppVersionVo selectAppVersion(){
		return this.dao.selectAppVersion();
	}

}
