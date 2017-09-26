package com.yfax.task.ytt.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yfax.task.ytt.dao.YttRateSetDao;
import com.yfax.task.ytt.vo.YttRateSetVo;

/**
 * 汇率配置数据
 * @author Minbo
 */
@Service
public class YttRateSetService{
	
	protected static Logger logger = LoggerFactory.getLogger(YttRateSetService.class);
	
	@Autowired
	private YttRateSetDao dao;
	
	public YttRateSetVo selectRateSet() {
		return this.dao.selectRateSet();
	}

}
