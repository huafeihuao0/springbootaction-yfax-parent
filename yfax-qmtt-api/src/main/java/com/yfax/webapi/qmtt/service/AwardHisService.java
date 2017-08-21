package com.yfax.webapi.qmtt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.utils.DateUtil;
import com.yfax.utils.JsonResult;
import com.yfax.utils.ResultCode;
import com.yfax.utils.UUID;
import com.yfax.webapi.qmtt.dao.AppUserDao;
import com.yfax.webapi.qmtt.dao.AwardHisDao;
import com.yfax.webapi.qmtt.vo.AppUserVo;
import com.yfax.webapi.qmtt.vo.AwardHisVo;

/**
 * 记录用户金币奖励记录
 * @author Minbo
 */
@Service
public class AwardHisService{
	
	protected static Logger logger = LoggerFactory.getLogger(AwardHisService.class);
	
	@Autowired
	private AwardHisDao awardHisDao;
	@Autowired
	private AppUserDao appUserDao;
	
	@Transactional
	public JsonResult addAwardHis(String phoneNum, int gold, Integer awardType){
		try {
			//1. 记录奖励明细
			AwardHisVo awardHisVo = new AwardHisVo();
			awardHisVo.setId(UUID.getUUID());
			awardHisVo.setPhoneNum(phoneNum);
			awardHisVo.setAwardType(awardType);
			awardHisVo.setGold(String.valueOf(gold));
			String cTime = DateUtil.getCurrentLongDateTime();
			awardHisVo.setCreateDate(cTime);
			awardHisVo.setUpdateDate(cTime);
			//2. 更新用户金币余额
			AppUserVo appUserVo = this.appUserDao.selectByPhoneNum(phoneNum);
			int old = Integer.valueOf(appUserVo.getGold());
			int sum = Integer.valueOf(appUserVo.getGold()) + gold;
			appUserVo.setGold(String.valueOf(sum));
			appUserVo.setUpdateDate(cTime);
			logger.info("手机号码phoneNum=" + phoneNum + ", 原金币余额gold=" + old + ", 奖励金币gold=" + gold 
				+ ", 更新金币总余额sum=" + sum + ", 奖励类型awardType=" + awardType);
			boolean flag =  this.awardHisDao.insertAwardHis(awardHisVo);
			boolean flag1 = this.appUserDao.updateUser(appUserVo);
			if(flag && flag1) {
				Map<String, Object> map = new HashMap<>();
				map.put("gold", gold);
				return new JsonResult(ResultCode.SUCCESS, map);
			}else {
				return new JsonResult(ResultCode.SUCCESS_FAIL);
			}
		} catch (Exception e) {
			logger.error("记录用户金币奖励记录异常：" + e.getMessage(), e);
			return new JsonResult(ResultCode.EXCEPTION);
		}
	}
	
	public List<AwardHisVo> selectAwardHisByPhoneNum(String phoneNum) {
		return this.awardHisDao.selectAwardHisByPhoneNum(phoneNum);
	}
	
	public AwardHisVo selectAwardHisIsCheckIn(Map<String, Object> map) {
		return this.awardHisDao.selectAwardHisIsCheckIn(map);
	}
	
}
