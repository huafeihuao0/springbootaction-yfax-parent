package com.yfax.task.ytt.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yfax.task.ytt.dao.YttAppConfigDao;
import com.yfax.task.ytt.vo.YttAppConfigVo;

/**
 * 信息配置数据
 * @author Minbo
 */
@Service
public class YttAppConfigService{
	
	protected static Logger logger = LoggerFactory.getLogger(YttAppConfigService.class);
	
	@Autowired
	private YttAppConfigDao dao;
	
	public YttAppConfigVo selectAppConfig() {
		return this.dao.selectAppConfig();
	}

}
