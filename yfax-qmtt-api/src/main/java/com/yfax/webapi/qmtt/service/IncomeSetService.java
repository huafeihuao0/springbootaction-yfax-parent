package com.yfax.webapi.qmtt.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yfax.webapi.qmtt.dao.IncomeSetDao;
import com.yfax.webapi.qmtt.vo.IncomeSetVo;

/**
 * 提现配置数据
 * @author Minbo
 */
@Service
public class IncomeSetService{
	
	protected static Logger logger = LoggerFactory.getLogger(IncomeSetService.class);
	
	@Autowired
	private IncomeSetDao dao;
	
	public List<IncomeSetVo> selectIncomeSet() {
		return this.dao.selectIncomeSet();
	}

}
