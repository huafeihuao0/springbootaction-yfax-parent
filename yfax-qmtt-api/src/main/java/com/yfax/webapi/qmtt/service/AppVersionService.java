package com.yfax.webapi.qmtt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yfax.webapi.qmtt.dao.AppVersionDao;
import com.yfax.webapi.qmtt.vo.AppVersionVo;

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
