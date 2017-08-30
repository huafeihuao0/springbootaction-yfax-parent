package com.yfax.webapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yfax.webapi.cfdb.vo.AppConfigVo;
import com.yfax.webapi.dao.AppConfigDao;

/**
 * APP初始化配置
 * @author Minbo
 */
@Service
public class AppConfigService{
	
	@Autowired
	private AppConfigDao dao;
	
	public AppConfigVo selectAppConfig(){
		return this.dao.selectAppConfig();
	}

}
