package com.yfax.webapi.qmtt.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yfax.webapi.qmtt.dao.AwardHisDao;
import com.yfax.webapi.qmtt.vo.AwardHisVo;

/**
 * 记录用户金币奖励记录
 * @author Minbo
 */
@Service
public class AwardHisService{
	
	protected static Logger logger = LoggerFactory.getLogger(AwardHisService.class);
	
	@Autowired
	private AwardHisDao dao;
	
	public boolean addAwardHis(AwardHisVo awardHisVo){
		try {
			return this.dao.insertAwardHis(awardHisVo);
		} catch (Exception e) {
			logger.error("记录用户金币奖励记录异常：" + e.getMessage(), e);
			return false;
		}
	}
	
	public List<AwardHisVo> selectAwardHisByPhoneNum(String phoneNum) {
		return this.dao.selectAwardHisByPhoneNum(phoneNum);
	}

}
