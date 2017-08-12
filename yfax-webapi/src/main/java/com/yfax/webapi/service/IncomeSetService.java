package com.yfax.webapi.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yfax.webapi.cfdb.vo.IncomeSetVo;
import com.yfax.webapi.dao.IncomeSetDao;

/**
 * 提现金额配置
 * @author Minbo
 */
@Service
public class IncomeSetService{
	
	@Autowired
	private IncomeSetDao dao;
	
	public List<IncomeSetVo> selectIncomeSetList(){
		return this.dao.selectIncomeSetList();
	}

}
