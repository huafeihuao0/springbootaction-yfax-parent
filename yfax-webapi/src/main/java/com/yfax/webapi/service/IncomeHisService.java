package com.yfax.webapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yfax.webapi.dao.IncomeHisDao;
import com.yfax.webapi.vo.IncomeHisVo;

/**
 * 收益记录
 * @author Minbo
 */
@Service
public class IncomeHisService{
	
	@Autowired
	private IncomeHisDao dao;
	
	public List<IncomeHisVo> selectIncomeHis(String phoneId){
		return this.dao.selectIncomeHis(phoneId);
	}

}
