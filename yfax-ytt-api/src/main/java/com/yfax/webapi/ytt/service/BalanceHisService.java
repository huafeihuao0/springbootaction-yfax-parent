package com.yfax.webapi.ytt.service;

import java.text.DecimalFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.utils.DateUtil;
import com.yfax.utils.JsonResult;
import com.yfax.utils.ResultCode;
import com.yfax.utils.UUID;
import com.yfax.webapi.GlobalUtils;
import com.yfax.webapi.ytt.dao.AppUserDao;
import com.yfax.webapi.ytt.dao.BalanceHisDao;
import com.yfax.webapi.ytt.dao.RateSetDao;
import com.yfax.webapi.ytt.vo.AppUserVo;
import com.yfax.webapi.ytt.vo.BalanceHisVo;
import com.yfax.webapi.ytt.vo.RateSetVo;

/**
 * 用户零钱兑换记录
 * @author Minbo
 */
@Service
public class BalanceHisService{
	
	protected static Logger logger = LoggerFactory.getLogger(BalanceHisService.class);
	
	@Autowired
	private BalanceHisDao dao;
	@Autowired
	private AppUserDao appUserDao;
	@Autowired
	private RateSetDao rateSetDao;
	
//	@Transactional
//	public JsonResult addBalanceHis(String phoneNum, String gold){
//		try {
//			DecimalFormat dFormat = new DecimalFormat(GlobalUtils.DECIMAL_FORMAT);
//			//得到当前汇率
//			RateSetVo rateSetVo = this.rateSetDao.selectRateSet();
//			//1. 记录零钱兑换记录
//			BalanceHisVo balanceHisVo = new BalanceHisVo();
//			balanceHisVo.setId(UUID.getUUID());
//			balanceHisVo.setPhoneNum(phoneNum);
//			balanceHisVo.setBalanceType(GlobalUtils.BALANCE_TYPE_REDEEM);
//			balanceHisVo.setGold(gold);
//			//3. 需要根据汇率计算，获得零钱
//			double rate = Double.valueOf(rateSetVo.getRate());
//			double goldDb = Double.valueOf(gold);
//			double balanceDb = goldDb * rate;
//			balanceHisVo.setBalance(dFormat.format(balanceDb));
//			balanceHisVo.setRate(rateSetVo.getRate());
//			logger.info("当前汇率rate=" + rateSetVo.getRate() + ", 兑换金币gold=" + goldDb + ", 获得零钱balance=" + balanceDb);
//			String cTime = DateUtil.getCurrentLongDateTime();
//			balanceHisVo.setCreateDate(cTime);
//			balanceHisVo.setUpdateDate(cTime);
//			//3. 扣减个人金币余额，更新用户金币余额
//			AppUserVo appUserVo = this.appUserDao.selectByPhoneNum(phoneNum);
//			int old = Integer.valueOf(appUserVo.getGold());
//			int sum = Integer.valueOf(appUserVo.getGold()) - Integer.valueOf(gold);
//			appUserVo.setGold(String.valueOf(sum));
//			appUserVo.setUpdateDate(cTime);
//			logger.info("手机号码phoneNum=" + phoneNum + ", 原金币余额gold=" + old + ", 扣减金币gold=" + gold 
//				+ ", 更新金币总余额sum=" + sum);
//			boolean flag = this.dao.insertBalanceHis(balanceHisVo);
//			boolean flag1 = this.appUserDao.updateUser(appUserVo);
//			if(flag && flag1) {
//				return new JsonResult(ResultCode.SUCCESS);
//			}else {
//				return new JsonResult(ResultCode.SUCCESS_FAIL);
//			}
//		} catch (Exception e) {
//			logger.error("记录用户零钱兑换异常：" + e.getMessage(), e);
//			return new JsonResult(ResultCode.EXCEPTION);
//		}
//	}
	
	public List<BalanceHisVo> selectBalanceHisByPhoneNum(String phoneNum) {
		return this.dao.selectBalanceHisByPhoneNum(phoneNum);
	}
	
}
