package com.yfax.webapi.qmtt.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yfax.webapi.qmtt.dao.InitConfigDao;
import com.yfax.webapi.qmtt.vo.InitConfigVo;

/**
 * 初始化配置数据
 * @author Minbo
 */
@Service
public class InitConfigService{
	
	protected static Logger logger = LoggerFactory.getLogger(InitConfigService.class);
	
	@Autowired
	private InitConfigDao dao;
	
	public InitConfigVo selectInitConfig() {
		return this.dao.selectInitConfig();
	}

}
