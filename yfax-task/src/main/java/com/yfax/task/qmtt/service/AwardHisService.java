package com.yfax.task.qmtt.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.task.qmtt.dao.AwardHisDao;
import com.yfax.task.qmtt.vo.AwardHisVo;

/**
 * 记录用户金币奖励记录
 * @author Minbo
 */
@Service
public class AwardHisService{
	
	protected static Logger logger = LoggerFactory.getLogger(AwardHisService.class);
	
	@Autowired
	private AwardHisDao awardHisDao;
	
	@Transactional
	public boolean addAwardHis(AwardHisVo awardHisVo){
		try {
			return this.awardHisDao.insertAwardHis(awardHisVo);
		} catch (Exception e) {
			logger.error("记录用户金币奖励记录异常：" + e.getMessage(), e);
			return false;
		}
	}
	
	public List<AwardHisVo> selectAwardHisByPhoneNum(String phoneNum) {
		return this.awardHisDao.selectAwardHisByPhoneNum(phoneNum);
	}
	
	public AwardHisVo selectAwardHisIsCheckIn(Map<String, Object> map) {
		return this.awardHisDao.selectAwardHisIsCheckIn(map);
	}
	
	public Long selectUserTotalOfGold(Map<String, Object> map) {
		return this.awardHisDao.selectUserTotalOfGold(map);
	}
	
}
