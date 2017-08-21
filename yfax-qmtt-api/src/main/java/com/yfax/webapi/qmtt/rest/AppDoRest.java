package com.yfax.webapi.qmtt.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yfax.common.sms.SmsService;
import com.yfax.utils.DateUtil;
import com.yfax.utils.JsonResult;
import com.yfax.utils.ResultCode;
import com.yfax.utils.StrUtil;
import com.yfax.utils.UUID;
import com.yfax.webapi.GlobalUtils;
import com.yfax.webapi.qmtt.service.AppUserService;
import com.yfax.webapi.qmtt.service.AwardHisService;
import com.yfax.webapi.qmtt.service.BalanceHisService;
import com.yfax.webapi.qmtt.service.LoginHisService;
import com.yfax.webapi.qmtt.service.UserSmsService;
import com.yfax.webapi.qmtt.service.WithdrawHisService;
import com.yfax.webapi.qmtt.vo.AppUserVo;
import com.yfax.webapi.qmtt.vo.AwardHisVo;
import com.yfax.webapi.qmtt.vo.LoginHisVo;
import com.yfax.webapi.qmtt.vo.UserSmsVo;

/**
 * @author Minbo.He
 * 受理接口
 */
@RestController
@RequestMapping("/api/qmtt")
public class AppDoRest {

	protected static Logger logger = LoggerFactory.getLogger(AppDoRest.class);
	
	@Autowired
	private AppUserService appUserService;
	@Autowired
	private UserSmsService userSmsService;
	@Autowired
	private LoginHisService loginHisService;
	@Autowired
	private AwardHisService awardHisService;
	@Autowired
	private BalanceHisService balanceHisService;
	@Autowired
	private WithdrawHisService withdrawHisService;
	
	/**
	 * 用户退出登录接口
	 */
	@RequestMapping(value = "/doLoginOut", method = {RequestMethod.POST})
	public JsonResult doLoginOut(String phoneNum) { 
		if(StrUtil.null2Str(phoneNum).equals("")) {
			return new JsonResult(ResultCode.PARAMS_ERROR);
		}
		boolean flag = this.appUserService.doLoginOut(phoneNum);
		if(flag) {
			return new JsonResult(ResultCode.SUCCESS);
		}
		return new JsonResult(ResultCode.SUCCESS_FAIL); 
	}
	
	/**
	 * 用户注册接口（限定手机号码）
	 */
	@RequestMapping(value = "/doRegister", method = {RequestMethod.POST})
	public JsonResult doRegister(String phoneNum, String userPwd) {
		AppUserVo appUserVo = this.appUserService.selectByPhoneNum(phoneNum);
		if(appUserVo != null) {
			return new JsonResult(ResultCode.SUCCESS_EXIST);
		}else {
			if(phoneNum == null || userPwd == null) {
				return new JsonResult(ResultCode.PARAMS_ERROR);
			}
			//添加用户
			appUserVo = new AppUserVo();
			appUserVo.setPhoneNum(phoneNum);;
			appUserVo.setUserPwd(userPwd);
			appUserVo.setGold("0");
			appUserVo.setBalance("0.00");
			String cTime = DateUtil.getCurrentLongDateTime();
			appUserVo.setRegisterDate(cTime);
			appUserVo.setLastLoginDate(cTime);
			appUserVo.setUpdateDate(cTime);
			boolean flag = this.appUserService.addUser(appUserVo);
			if(flag) {
				return new JsonResult(ResultCode.SUCCESS);
			}else {
				return new JsonResult(ResultCode.SUCCESS_FAIL);
			}
		}
	}
	
	/**
	 * 用户个人信息接口（限定手机号码）
	 */
	@RequestMapping(value = "/doUserInfo", method = {RequestMethod.POST})
	public JsonResult doUserInfo(String phoneNum, String userPwd, String userName, String address, 
			String wechat, String qq, String email) {
		AppUserVo appUserVo = new AppUserVo();
		appUserVo.setPhoneNum(phoneNum);
		appUserVo.setUserPwd(userPwd);
		appUserVo.setUserName(userName);
		appUserVo.setAddress(address);
		appUserVo.setWechat(wechat);
		appUserVo.setQq(qq);
		appUserVo.setEmail(email);
		String cTime = DateUtil.getCurrentLongDateTime();
		appUserVo.setUpdateDate(cTime);
		boolean flag = this.appUserService.modifyUser(appUserVo);
		if(flag) {
			return new JsonResult(ResultCode.SUCCESS);
		}else {
			return new JsonResult(ResultCode.SUCCESS_FAIL);
		}
	}
	
	/**
	 * 用户登录历史接口（限定手机号码）
	 */
	@RequestMapping(value = "/doLoginInfo", method = {RequestMethod.POST})
	public JsonResult doLoginInfo(String phoneNum, String data) {
		return null;
	}
	
	/**
	 * 短信验证码接口
	 */
	@RequestMapping(value = "/doSms", method = {RequestMethod.POST})
	public JsonResult doSms(String phoneNum, String msgCode) {
		if(!StrUtil.null2Str(phoneNum).equals("") && !StrUtil.null2Str(msgCode).equals("")) {
			HashMap<String, Object> result = SmsService.sendSms(phoneNum, msgCode, GlobalUtils.PROJECT_QMTT);
			if("000000".equals(result.get("statusCode"))){
				UserSmsVo userSms = new UserSmsVo();
				userSms.setId(UUID.getUUID());
				userSms.setPhoneNum(phoneNum);
				userSms.setMsgCode(msgCode);
				userSms.setProjectCode(GlobalUtils.PROJECT_QMTT);
				String cTime = DateUtil.getCurrentLongDateTime();
				userSms.setCreateDate(cTime);
				userSms.setUpdateDate(cTime);
				boolean flag = this.userSmsService.addUserSms(userSms);
				if(!flag) {
					logger.warn("短信记录失败，请查核");
				}
				return new JsonResult(ResultCode.SUCCESS, result);
			}else{
				return new JsonResult(ResultCode.SUCCESS_FAIL, result);
			}
		}else {
			return new JsonResult(ResultCode.PARAMS_ERROR);
		}
	}
	
	/**
	 * 记录用户登录设备数据接口
	 */
	@RequestMapping(value = "/doLoginHis", method = {RequestMethod.POST})
	public JsonResult doLoginHis(String phoneNum, String deviceName, String imei, String ip, 
			String mac, String location, String wifi) {
		if(!StrUtil.null2Str(phoneNum).equals("")) {
			AppUserVo appUserVo = this.appUserService.selectByPhoneNum(phoneNum);
			if(appUserVo != null) {
				LoginHisVo loginHisVo = new LoginHisVo();
				loginHisVo.setId(UUID.getUUID());
				loginHisVo.setPhoneNum(phoneNum);
				loginHisVo.setDeviceName(deviceName);
				loginHisVo.setImei(imei);
				loginHisVo.setIp(ip);
				loginHisVo.setMac(mac);
				loginHisVo.setLocation(location);
				loginHisVo.setWifi(wifi);
				String cTime = DateUtil.getCurrentLongDateTime();
				loginHisVo.setCreateDate(cTime);
				loginHisVo.setUpdateDate(cTime);
				boolean flag = this.loginHisService.addLoginHis(loginHisVo);
				if(flag) {
					return new JsonResult(ResultCode.SUCCESS);
				}else {
					return new JsonResult(ResultCode.SUCCESS_FAIL);
				}
			}else {
				return new JsonResult(ResultCode.SUCCESS_NO_USER);
			}
		}else {
			return new JsonResult(ResultCode.PARAMS_ERROR);
		}
	}
	
	/**
	 * 生成分享邀请链接
	 */
	@RequestMapping(value = "/doShareUrl", method = {RequestMethod.POST})
	public JsonResult doShareUrl() {
		return new JsonResult(ResultCode.SUCCESS);
	}
	
	/**
	 * 获得阅读文章随机金币
	 */
	@RequestMapping(value = "/doRandomAward", method = {RequestMethod.POST})
	public JsonResult doRandomAward(String phoneNum) {
		//随机金币奖励
		int gold = GlobalUtils.gold[new Random().nextInt(9)];
		return this.awardHisService.addAwardHis(phoneNum, gold, GlobalUtils.AWARD_TYPE_READ);
		
	}
	
	/**
	 * 用户发起零钱兑换接口
	 */
	@RequestMapping(value = "/doBalanceHis", method = {RequestMethod.POST})
	public JsonResult doBalanceHis(String phoneNum, String gold) {
		return this.balanceHisService.addBalanceHis(phoneNum, gold);
	}
	
	/**
	 * 提现发起接口
	 */
	@RequestMapping(value = "/doWithdraw", method = {RequestMethod.POST})
	public JsonResult doWithdraw(String phoneNum, int withdrawType, 
			String name, String account, String income) {
		if(!StrUtil.null2Str(phoneNum).equals("") && !StrUtil.null2Str(account).equals("") 
				&& !StrUtil.null2Str(income).equals("")) {
			AppUserVo appUserVo = this.appUserService.selectByPhoneNum(phoneNum);
			if(appUserVo != null) {
				double incomeTmp = Double.valueOf(income);
				double balance = Double.valueOf(appUserVo.getBalance());
				if(balance - incomeTmp >= 0) {
					return this.withdrawHisService.addWithdrawHis(phoneNum, withdrawType, 
							name, account, income);
				}else {
					return new JsonResult(ResultCode.SUCCESS_NOT_ENOUGH);
				}
			}else {
				return new JsonResult(ResultCode.SUCCESS_NO_USER);
			}
		}else {
			return new JsonResult(ResultCode.PARAMS_ERROR);
		}
	}
	
	/**
	 * 重置用户登录密码
	 */
	@RequestMapping(value = "/doResetPwd", method = {RequestMethod.POST})
	public JsonResult doResetPwd(String phoneNum, String userPwd) {
		if(!StrUtil.null2Str(phoneNum).equals("") && !StrUtil.null2Str(userPwd).equals("")) {
			AppUserVo appUserVo = new AppUserVo();
			appUserVo.setPhoneNum(phoneNum);
			appUserVo.setUserPwd(userPwd);
			String cTime = DateUtil.getCurrentLongDateTime();
			appUserVo.setUpdateDate(cTime);
			boolean flag = this.appUserService.modifyUser(appUserVo);
			if(flag) {
				return new JsonResult(ResultCode.SUCCESS);
			}else {
				return new JsonResult(ResultCode.SUCCESS_FAIL);
			}
		}else {
			return new JsonResult(ResultCode.PARAMS_ERROR);
		}
	}
	
	/**
	 * 每日签到获得随机金币
	 */
	@RequestMapping(value = "/doDailyCheckIn", method = {RequestMethod.POST})
	public JsonResult doDailyCheckIn(String phoneNum) {
		if(!StrUtil.null2Str(phoneNum).equals("")) {
			String currentTime = DateUtil.getCurrentDate();
			Map<String, Object> map = new HashMap<>();
			map.put("phoneNum", phoneNum);
			map.put("currentTime", currentTime);
			AwardHisVo awardHisVo = this.awardHisService.selectAwardHisIsCheckIn(map);
			if(awardHisVo == null) {
				//随机金币奖励
				int gold = GlobalUtils.gold[new Random().nextInt(9)];
				return this.awardHisService.addAwardHis(phoneNum, gold, GlobalUtils.AWARD_TYPE_DAYLY);
			}else {
				return new JsonResult(ResultCode.SUCCESS_CHECK_IN);
			}
		}else {
			return new JsonResult(ResultCode.PARAMS_ERROR);
		}
	}
}