package com.yfax.task.ytt.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.task.ytt.dao.YttAwardHisDao;
import com.yfax.task.ytt.vo.YttAwardHisVo;

/**
 * 记录用户金币奖励记录
 * @author Minbo
 */
@Service
public class YttAwardHisService{
	
	protected static Logger logger = LoggerFactory.getLogger(YttAwardHisService.class);
	
	@Autowired
	private YttAwardHisDao dao;
	
	@Transactional
	public boolean addAwardHis(YttAwardHisVo awardHisVo){
		try {
			return this.dao.insertAwardHis(awardHisVo);
		} catch (Exception e) {
			logger.error("记录用户金币奖励记录异常：" + e.getMessage(), e);
			return false;
		}
	}
	
	public List<YttAwardHisVo> selectAwardHisByPhoneNum(String phoneNum) {
		return this.dao.selectAwardHisByPhoneNum(phoneNum);
	}
	
	public YttAwardHisVo selectAwardHisIsCheckIn(Map<String, Object> map) {
		return this.dao.selectAwardHisIsCheckIn(map);
	}
	
	public Long selectUserTotalOfGold(Map<String, Object> map) {
		return this.dao.selectUserTotalOfGold(map);
	}
	
}
