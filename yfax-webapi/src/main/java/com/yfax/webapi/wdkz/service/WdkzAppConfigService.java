package com.yfax.webapi.wdkz.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yfax.webapi.wdkz.dao.WdkzAppConfigDao;
import com.yfax.webapi.wdkz.vo.WdkzAppConfigVo;

/**
 * 微多开助手，app版本配置信息
 * @author Minbo
 */
@Service
public class WdkzAppConfigService{
	
	protected static Logger logger = LoggerFactory.getLogger(WdkzAppConfigService.class);
	
	@Autowired
	private WdkzAppConfigDao dao;
	
	public WdkzAppConfigVo selectAppConfigByVersion(String version){
		return this.dao.selectAppConfigByVersion(version);
	}
}
