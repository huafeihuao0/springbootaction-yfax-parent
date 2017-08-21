package com.yfax.webapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yfax.webapi.cfdb.vo.AppVersionVo;
import com.yfax.webapi.dao.AppVersionDao;

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
