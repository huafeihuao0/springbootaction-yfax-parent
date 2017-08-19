package com.yfax.webapi.qmtt.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yfax.webapi.qmtt.dao.BalanceHisDao;
import com.yfax.webapi.qmtt.vo.BalanceHisVo;

/**
 * 用户零钱兑换记录
 * @author Minbo
 */
@Service
public class BalanceHisService{
	
	protected static Logger logger = LoggerFactory.getLogger(BalanceHisService.class);
	
	@Autowired
	private BalanceHisDao dao;
	
	public boolean addBalanceHis(BalanceHisVo balanceHisVo){
		try {
			return this.dao.insertBalanceHis(balanceHisVo);
		} catch (Exception e) {
			logger.error("记录用户零钱兑换异常：" + e.getMessage(), e);
			return false;
		}
	}
	
	public List<BalanceHisVo> selectBalanceHisByPhoneNum(String phoneNum) {
		return this.dao.selectBalanceHisByPhoneNum(phoneNum);
	}

}
