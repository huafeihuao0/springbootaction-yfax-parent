package com.yfax.webapi.ytt.rest;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yfax.common.sms.SmsService;
import com.yfax.utils.DateUtil;
import com.yfax.utils.JsonResult;
import com.yfax.utils.NetworkUtil;
import com.yfax.utils.ResultCode;
import com.yfax.utils.ShareCodeUtil;
import com.yfax.utils.StrUtil;
import com.yfax.utils.UUID;
import com.yfax.webapi.GlobalUtils;
import com.yfax.webapi.ytt.service.AppConfigService;
import com.yfax.webapi.ytt.service.AppShareCodeService;
import com.yfax.webapi.ytt.service.AppUserService;
import com.yfax.webapi.ytt.service.AwardHisService;
import com.yfax.webapi.ytt.service.InitConfigService;
import com.yfax.webapi.ytt.service.IpShareCodeService;
import com.yfax.webapi.ytt.service.LoginHisService;
import com.yfax.webapi.ytt.service.ReadHisService;
import com.yfax.webapi.ytt.service.UserFeedbackService;
import com.yfax.webapi.ytt.service.UserSmsService;
import com.yfax.webapi.ytt.service.WithdrawHisService;
import com.yfax.webapi.ytt.vo.AppConfigVo;
import com.yfax.webapi.ytt.vo.AppShareCodeVo;
import com.yfax.webapi.ytt.vo.AppUserVo;
import com.yfax.webapi.ytt.vo.InitConfigVo;
import com.yfax.webapi.ytt.vo.IpShareCodeVo;
import com.yfax.webapi.ytt.vo.LoginHisVo;
import com.yfax.webapi.ytt.vo.ReadHisVo;
import com.yfax.webapi.ytt.vo.UserFeedbackVo;
import com.yfax.webapi.ytt.vo.UserSmsVo;

/**
 * @author Minbo.He
 * 受理接口
 */
@RestController
@RequestMapping("/api/ytt")
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
//	@Autowired
//	private BalanceHisService balanceHisService;
	@Autowired
	private WithdrawHisService withdrawHisService;
	@Autowired
	private ReadHisService readHisService;
	@Autowired
	private AppShareCodeService appShareCodeService;
	@Autowired
	private IpShareCodeService ipShareCodeService;
	@Autowired
	private UserFeedbackService userFeedbackService;
	@Autowired
	private AppConfigService appConfigService;
	@Autowired
	private InitConfigService initConfigService;
	
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
	public JsonResult doRegister(String phoneNum, String userPwd, HttpServletRequest request) {
		AppUserVo appUserVo = this.appUserService.selectByPhoneNum(phoneNum);
		if(appUserVo != null) {
			return new JsonResult(ResultCode.SUCCESS_EXIST);
		}else {
			if(phoneNum == null || userPwd == null) {
				return new JsonResult(ResultCode.PARAMS_ERROR);
			}
			return this.appUserService.registerUser(phoneNum, userPwd, request);
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
	 * 短信验证码接口
	 */
	@RequestMapping(value = "/doSms", method = {RequestMethod.POST})
	public JsonResult doSms(String phoneNum, String msgCode) {
		if(!StrUtil.null2Str(phoneNum).equals("") && !StrUtil.null2Str(msgCode).equals("")) {
			HashMap<String, Object> result = SmsService.sendSms(phoneNum, msgCode, GlobalUtils.PROJECT_YTT
					, GlobalUtils.SMS_TEMPLATE_ID, GlobalUtils.SMS_APP_ID);
			if("000000".equals(result.get("statusCode"))){
				UserSmsVo userSms = new UserSmsVo();
				userSms.setId(UUID.getUUID());
				userSms.setPhoneNum(phoneNum);
				userSms.setMsgCode(msgCode);
				userSms.setProjectCode(GlobalUtils.PROJECT_YTT);
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
	public JsonResult doShareUrl(String phoneNum) {
		AppShareCodeVo appShareCodeVo = this.appShareCodeService.selectAppShareCodeByPhoneNum(phoneNum);
		if(appShareCodeVo == null) {
			appShareCodeVo = new AppShareCodeVo();
			appShareCodeVo.setPhoneNum(phoneNum);
			appShareCodeVo.setShareCode(ShareCodeUtil.toSerialCode(Long.valueOf(phoneNum)));
			String cTime = DateUtil.getCurrentLongDateTime();
			appShareCodeVo.setCreateDate(cTime);
			appShareCodeVo.setUpdateDate(cTime);
			boolean flag = this.appShareCodeService.addAppShareCode(appShareCodeVo);
			if(flag) {
				InitConfigVo initConfigVo = this.initConfigService.selectInitConfig();
				//返回邀请中转链接
				logger.info("新创建的，分享邀请链接：phoneNum=" + phoneNum + ", shareCode=" + appShareCodeVo.getShareCode());
				return new JsonResult(ResultCode.SUCCESS, initConfigVo.getAppInviteUrl() + appShareCodeVo.getShareCode());
			}else {
				logger.error("分享邀请链接生成失败", new RuntimeException("phoneNum=" + phoneNum));
				return new JsonResult(ResultCode.SUCCESS_FAIL);
			}
		}else {
			InitConfigVo initConfigVo = this.initConfigService.selectInitConfig();
			logger.info("已存在的，分享邀请链接：phoneNum=" + phoneNum + ", shareCode=" + appShareCodeVo.getShareCode());
			return new JsonResult(ResultCode.SUCCESS, initConfigVo.getAppInviteUrl() + appShareCodeVo.getShareCode());
		}
	}
	
	/**
	 * 邀请中转链接接口
	 * @return
	 */
	@RequestMapping(value = "/doRedirectUrl", method = {RequestMethod.GET})
	public JsonResult doRedirectUrl(String shareCode, HttpServletRequest request, HttpServletResponse response) {
		 String sourceIp = NetworkUtil.getIpAddress(request);
		 logger.info("邀请链接中转接口, 获取访问者IP=" + sourceIp + ", 邀请码shareCode=" + shareCode);
		 AppShareCodeVo appShareCodeVo = this.appShareCodeService.selectAppShareCodeByShareCode(shareCode);
		 if(appShareCodeVo == null) {
			 logger.warn("无效邀请码，不存在的邀请码。shareCode=" + shareCode);
			 return new JsonResult(ResultCode.SUCCESS_INVALID_CODE);
		 }
		 Enumeration<String> names = request.getHeaderNames();
		 String url = "";
		 while (names.hasMoreElements()){
			 String name = (String) names.nextElement();
			 if(request.getHeader(name).contains("iPhone")){
				//配置信息
				 AppConfigVo appConfigVo = this.appConfigService.selectAppConfig();
				 url = appConfigVo.getIphoneUrl();
				 break;
			 }else if(request.getHeader(name).contains("Android")) {
				//配置信息
				 AppConfigVo appConfigVo = this.appConfigService.selectAppConfig();
				 url = appConfigVo.getAndroidUrl();
				 break;
			 }
		 }
		 logger.info("跳转url=" + url);
		 try {
			 if(!url.equals("")) {
				 IpShareCodeVo ipShareCodeVo = new IpShareCodeVo();
				 ipShareCodeVo.setId(UUID.getUUID());
				 ipShareCodeVo.setSourceIp(sourceIp);
				 ipShareCodeVo.setShareCode(shareCode);
				 ipShareCodeVo.setIsUsed(1);//未使用
				 String cTime = DateUtil.getCurrentLongDateTime();
				 ipShareCodeVo.setCreateDate(cTime);
				 ipShareCodeVo.setUpdateDate(cTime);
				 boolean flag = this.ipShareCodeService.addIpShareCode(ipShareCodeVo);
				 if(!flag) {
					 logger.warn("新增失败，ipShareCodeVo=" + ipShareCodeVo.toString());
				 }
				 if(!url.equals("")) {
					 InitConfigVo initConfigVo = this.initConfigService.selectInitConfig();
					 response.sendRedirect(initConfigVo.getPageUrl()  + initConfigVo.getDownloadUrl());
				 }
			 }
		} catch (IOException e) {
			logger.error("跳转异常：" + e.getMessage(), e);
		}
		return new JsonResult(ResultCode.SUCCESS);
	}
	
	/**
	 * 下载APP接口
	 * @return
	 */
	@RequestMapping(value = "/doDownloadUrl", method = {RequestMethod.GET})
	public JsonResult doDownloadUrl(String type, HttpServletRequest request, HttpServletResponse response) {
		 Enumeration<String> names = request.getHeaderNames();
		 String url = "";
		 while (names.hasMoreElements()){
			 String name = (String) names.nextElement();
			 if(request.getHeader(name).contains("iPhone")){
				//配置信息
				 AppConfigVo appConfigVo = this.appConfigService.selectAppConfig();
				 url = appConfigVo.getIphoneUrl();
				 break;
			 }else if(request.getHeader(name).contains("Android")) {
				//配置信息
				 AppConfigVo appConfigVo = this.appConfigService.selectAppConfig();
				 url = appConfigVo.getAndroidUrl();
				 break;
			 }
		 }
		 logger.info("下载APP url=" + url);
		 try {
			 if(!url.equals("")) {
				 response.sendRedirect(url);
			 }
		} catch (IOException e) {
			logger.error("跳转异常：" + e.getMessage(), e);
		}
		return new JsonResult(ResultCode.SUCCESS);
	}
	
	/**
	 * 获得阅读文章随机金币
	 */
	@RequestMapping(value = "/doReadAward", method = {RequestMethod.POST})
	public JsonResult doReadAward(String phoneNum, String readHisId) {
		if(!StrUtil.null2Str(phoneNum).equals("") && !StrUtil.null2Str(readHisId).equals("")) {
			//1. 先判断奖励文章是否存在
			ReadHisVo readHisVo = this.readHisService.selectReadHisById(readHisId);
			if(readHisVo == null) {
				return new JsonResult(ResultCode.SUCCESS_NO_DATA);
			}
			//配置信息
			AppConfigVo appConfigVo = this.appConfigService.selectAppConfig();
			Map<String, Object> map = new HashMap<>();
			map.put("phoneNum", phoneNum);
			//先判断是否达到每日阅读奖励上限
			map.put("awardType", GlobalUtils.AWARD_TYPE_READ);
			map.put("currentTime", DateUtil.getCurrentDate());
			Long total = this.awardHisService.selectUserTotalOfGold(map);		//统计个人今日阅读领取金币
			if(total>=appConfigVo.getGoldLimit()) {
				return new JsonResult(ResultCode.SUCCESS_DAILY_LIMIT);
			}
			//2. 首次阅读才奖励固定金币
			AppUserVo appUserVo = this.appUserService.selectByPhoneNum(phoneNum);
			if(appUserVo.getFirstRead() == 0) {
				logger.info("首次有效阅读固定奖励，gold=" + appConfigVo.getFirstReadGold() + "，phoneNum=" + phoneNum);
				return this.awardHisService.addAwardHis(phoneNum, appConfigVo.getFirstReadGold()
						, GlobalUtils.AWARD_TYPE_FIRSTREAD, 1, null, null, readHisId, null);
			}
			//3. 一篇文章只能奖励一次
			if(readHisVo.getIsAward() == 0){
				//随机金币奖励
				int gold = GlobalUtils.getRanomGold(appConfigVo.getGoldRange());
				logger.info("阅读随机奖励，gold=" + gold + "，phoneNum=" + phoneNum);
				return this.awardHisService.addAwardHis(phoneNum, gold, 
						GlobalUtils.AWARD_TYPE_READ, null, null, null, readHisId, null);
				
			}else if(readHisVo.getIsAward() == 1) {
				String result = "文章已获取奖励，跳过处理";
				logger.info(result + "。phoneNum=" + phoneNum + ", readHisId=" + readHisId);
				return new JsonResult(ResultCode.SUCCESS_DUPLICATE, result);
			}
			return new JsonResult(ResultCode.SUCCESS_ALL_GONE);
		}else {
			return new JsonResult(ResultCode.PARAMS_ERROR);
		}
	}
	
//	/**
//	 * 用户发起零钱兑换接口-废弃
//	 */
//	@RequestMapping(value = "/doBalanceHis", method = {RequestMethod.POST})
//	public JsonResult doBalanceHis(String phoneNum, String gold) {
//		return this.balanceHisService.addBalanceHis(phoneNum, gold);
//	}
	
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
							name, account, income, appUserVo);
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
			AppUserVo appUserVo = this.appUserService.selectByPhoneNum(phoneNum);
			if(appUserVo.getDailyCheckIn() == 0) {
				//配置信息
				AppConfigVo appConfigVo = this.appConfigService.selectAppConfig();
				//随机金币奖励
				int gold = GlobalUtils.getRanomGold(appConfigVo.getGoldRange());
				return this.awardHisService.addAwardHis(phoneNum, gold, 
						GlobalUtils.AWARD_TYPE_DAYLY, null, null, null, null, 1);
			}else {
				return new JsonResult(ResultCode.SUCCESS_CHECK_IN);
			}
		}else {
			return new JsonResult(ResultCode.PARAMS_ERROR);
		}
	}
	
	/**
	 * 记录阅读文章历史
	 */
	@RequestMapping(value = "/doReadHis", method = {RequestMethod.POST})
	public JsonResult doReadHis(String phoneNum, String data, String primaryKey) {
		if(!StrUtil.null2Str(phoneNum).equals("") && !StrUtil.null2Str(data).equals("") 
				&& !StrUtil.null2Str(primaryKey).equals("")) {
			Map<String, Object> map = new HashMap<>();
			map.put("phoneNum", phoneNum);
			map.put("primaryKey", primaryKey);
			//1. 先判断历史文章是否已存在
			ReadHisVo readHisVo = this.readHisService.selectReadHisByPhoneNumAndPrimaryKey(map);
			boolean flag = false;
			if(readHisVo == null) {
				readHisVo = new ReadHisVo();
				String readHisId = UUID.getUUID();
				readHisVo.setId(readHisId);
				readHisVo.setPhoneNum(phoneNum);
				readHisVo.setData(data);
				readHisVo.setPrimaryKey(primaryKey);
				readHisVo.setIsAward(0); //新建时默认未奖励
				String cTime = DateUtil.getCurrentLongDateTime();
				readHisVo.setCreateDate(cTime);
				readHisVo.setUpdateDate(cTime);
				flag = this.readHisService.addReadHis(readHisVo);
			}else {
				readHisVo.setUpdateDate(DateUtil.getCurrentLongDateTime());
				flag = this.readHisService.modifyReadHis(readHisVo);
			}
			if(flag) {
				Map<String, Object> result = new HashMap<>();
				result.put("readHisId", readHisVo.getId());
				result.put("isAward", readHisVo.getIsAward());
				return new JsonResult(ResultCode.SUCCESS, result);
			}else {
				return new JsonResult(ResultCode.SUCCESS_FAIL);
			}
		}else {
			return new JsonResult(ResultCode.PARAMS_ERROR);
		}
	}
	
	/**
	 * 首次分享奖励金币接口
	 */
	@RequestMapping("/doSocialShare")
	public JsonResult doSocialShare(String phoneNum) {
		AppUserVo appUserVo = this.appUserService.selectByPhoneNum(phoneNum);
		if(appUserVo.getFirstShare() == 0) {
			appUserVo.setFirstShare(1);
			appUserVo.setUpdateDate(DateUtil.getCurrentLongDateTime());
			boolean flag = this.appUserService.modifyUser(appUserVo);
			if(flag) {
				//配置信息
				AppConfigVo appConfigVo = this.appConfigService.selectAppConfig();
				return this.awardHisService.addAwardHis(phoneNum, appConfigVo.getFirstShareGold()
						, GlobalUtils.AWARD_TYPE_FIRSTSHARE, null, 1, null, null, null);
			}else {
				return new JsonResult(ResultCode.SUCCESS_FAIL);
			}
		}else {
			return new JsonResult(ResultCode.SUCCESS_DUPLICATE);
		}
	}
	
	/**
	 * 用户反馈信息接口
	 */
	@RequestMapping(value = "/doUserFeedback", method = {RequestMethod.POST})
	public JsonResult doUserFeedback(String phoneNum, String info) {
		if(!StrUtil.null2Str(phoneNum).equals("") && !StrUtil.null2Str(info).equals("")) {
			UserFeedbackVo userFeedbackVo = new UserFeedbackVo();
			userFeedbackVo.setId(UUID.getUUID());
			userFeedbackVo.setPhoneNum(phoneNum);
			userFeedbackVo.setInfo(info);
			String cTime = DateUtil.getCurrentLongDateTime();
			userFeedbackVo.setCreateDate(cTime);
			userFeedbackVo.setUpdateDate(cTime);
			boolean result = this.userFeedbackService.addUserFeedback(userFeedbackVo);
			if(result) {
				return new JsonResult(ResultCode.SUCCESS);
			}else{
				return new JsonResult(ResultCode.SUCCESS_FAIL);
			}
		}else {
			return new JsonResult(ResultCode.PARAMS_ERROR);
		}
	}
	
	/**
	 * rest推送测试用接口
	 * 参数说明：
	 * phoneNum，手机号码
	 * type，消息类型（1=通知栏消息，2=透传消息）
	 */
	@RequestMapping("/testPushNotify")
	public String testPushNotify(String phoneNum, int type) {
		return this.appUserService.testPushNotify(phoneNum, type);
	}
}