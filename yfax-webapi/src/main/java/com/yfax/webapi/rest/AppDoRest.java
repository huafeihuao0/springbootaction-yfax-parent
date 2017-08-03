package com.yfax.webapi.rest;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yfax.webapi.service.AdvHisService;
import com.yfax.webapi.service.SdkTasklistService;
import com.yfax.webapi.service.UserFeedbackService;
import com.yfax.webapi.service.UserTaskListService;
import com.yfax.webapi.service.UsersService;
import com.yfax.webapi.service.WithdrawHisService;
import com.yfax.webapi.sms.SmsService;
import com.yfax.webapi.utils.DateUtil;
import com.yfax.webapi.utils.JsonResult;
import com.yfax.webapi.utils.MD5Util;
import com.yfax.webapi.utils.ResultCode;
import com.yfax.webapi.utils.StrUtil;
import com.yfax.webapi.utils.UUID;
import com.yfax.webapi.vo.AdvHisVo;
import com.yfax.webapi.vo.SdkTasklistVo;
import com.yfax.webapi.vo.UserFeedbackVo;
import com.yfax.webapi.vo.UsersVo;

/**
 * @author Minbo.He
 * 受理接口
 */
@RestController
@RequestMapping("/api/cfdb")
public class AppDoRest {

	protected static Logger logger = LoggerFactory.getLogger(AppDoRest.class);
	//手机IMEI长度15位，固定值
	private final static int IMEI_LENGTH = 15;
	
	@Autowired
	private UsersService usersService;
	@Autowired
	private UserTaskListService userTaskListService;
	@Autowired
	private WithdrawHisService withdrawHisService;
	@Autowired
	private AdvHisService advHisService;
	@Autowired
	private SdkTasklistService sdkTasklistService;
	@Autowired
	private UserFeedbackService userFeedbackService;
	
	/**
	 * 用户登录接口（限定手机IM码）
	 */
	@RequestMapping("/doLogin")
	public JsonResult doLogin(String phoneId, HttpServletRequest request) {
		if(phoneId.length() == IMEI_LENGTH) {
			UsersVo users = this.usersService.selectUsersByPhoneId(phoneId);
			if(users == null) {
				boolean result = this.usersService.addUser(phoneId);
				if(!result) {
					return new JsonResult(ResultCode.SUCCESS_FAIL);
				}
			}
			Map<String,String> map = new HashMap<String, String>();
			String tokenId = UUID.getUUID();
			map.put("tokenId", tokenId);
			//默认session失效时间为30分钟
			request.getSession().setAttribute("_session_tokenId", tokenId);
			return new JsonResult(ResultCode.SUCCESS, map);
		}else {
			return new JsonResult(ResultCode.IMEI_ERROR);
		}
	}
	
	/**
	 * 用户抢购任务接口
	 */
	@RequestMapping("/doPanicBuying")
	public JsonResult doPanicBuying(String phoneId, String taskId) {
		UsersVo users = this.usersService.selectUsersByPhoneId(phoneId);
		if(users != null) {
			return this.userTaskListService.doPanicBuying(phoneId, taskId);
		}else {
			return new JsonResult(ResultCode.SUCCESS_NO_USER);
		}
	}
	
	/**
	 * 用户放弃任务接口
	 */
	@RequestMapping("/doAbandonTask")
	public JsonResult doAbandonTask(String phoneId, String taskId) {
		UsersVo users = this.usersService.selectUsersByPhoneId(phoneId);
		if(users != null) {
			return this.userTaskListService.abandonUserTask(phoneId, taskId);
		}else {
			return new JsonResult(ResultCode.SUCCESS_NO_USER);
		}
	}
	
	/**
	 * 提交审核任务接口
	 */
	@RequestMapping(value = "/doProve", method = {RequestMethod.GET, RequestMethod.POST})
	public JsonResult doProve(String phoneId, String id, String fields) {
		if(!StrUtil.null2Str(phoneId).equals("") && !StrUtil.null2Str(id).equals("") 
				&& !StrUtil.null2Str(fields).equals("")) {
			UsersVo users = this.usersService.selectUsersByPhoneId(phoneId);
			if(users != null) {
				return this.userTaskListService.doProve(phoneId, id, fields);
			}else {
				return new JsonResult(ResultCode.SUCCESS_NO_USER);
			}
		}else {
			return new JsonResult(ResultCode.PARAMS_ERROR);
		}
	}

	/**
	 * 提现发起接口
	 */
	@RequestMapping("/doWithdraw")
	public JsonResult doWithdraw(String phoneId, int withdrawType, 
			String name, String account, String income) {
		if(!StrUtil.null2Str(phoneId).equals("") && !StrUtil.null2Str(account).equals("") 
				&& !StrUtil.null2Str(income).equals("")) {
			UsersVo user = this.usersService.selectUsersByPhoneId(phoneId);
			if(user != null) {
				int incomeTmp = Integer.valueOf(income);
				int balance = Integer.valueOf(user.getBalance());
				if(balance - incomeTmp >= 0) {
					return this.withdrawHisService.addWithdrawHis(phoneId, withdrawType, 
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
	 * 用户已点击任务查看记录接口
	 */
	@RequestMapping("/doCheck")
	public JsonResult doCheck(String phoneId, String id) {
		UsersVo users = this.usersService.selectUsersByPhoneId(phoneId);
		if(users != null) {
			boolean result = this.userTaskListService.setIsChecked(id);
			if(result) {
				return new JsonResult(ResultCode.SUCCESS);
			}else{
				return new JsonResult(ResultCode.SUCCESS_NO_DATA);
			}
		}else {
			return new JsonResult(ResultCode.SUCCESS_NO_USER);
		}
	}
	
	/**
	 * 广告平台接口回调数据秘钥
	 */
	private final static String AD_SECRET="*#bUOyFBOI#(@BFW";
	
	/**
	 * 广告平台状态回调接口
	 */
	@RequestMapping("/sendAdvInfo")
	public String sendAdvInfo(String hashid,String appid,String adid,
			String adname,String userid,String mac,String deviceid,
			String source,String point,String time,String active_num,
			String checksum) {
		//1. 校验MD5数据内容
		String parameter= "?hashid=" + hashid + "&appid=" + appid + "&adid=" + adid + "&adname=" + adname + ""
				+ "&userid=" + userid + "&mac=" + mac + "&deviceid=" + deviceid + ""
				+ "&source=" + source + "&point=" + point + "&time=" + time 
				+ "&active_num=" + active_num + "&appsecret=" + AD_SECRET + "";
		logger.info("parameter=[" + parameter+"]");
		String md5Result = MD5Util.encodeByMD5(parameter)	.toLowerCase();	//小写比较	
		if(hashid == null || appid == null || checksum == null) {
			logger.error("参数错误");
			return "{\"message\":\"参数错误\",\"success\":\"false\"}";
		}
		//md5校对结果
		boolean flag = md5Result.equals(checksum.toLowerCase());
		//2. 保存广告平台回调记录
		AdvHisVo advHis = new AdvHisVo();
		advHis.setHashid(hashid);
		advHis.setAppid(appid);;
		advHis.setAdid(adid);
		advHis.setAdname(adname);
		advHis.setUserid(userid);
		advHis.setMac(mac);
		advHis.setDeviceid(deviceid);
		advHis.setSource(source);
		advHis.setPoint(point);
		advHis.setTime(time);
		advHis.setTimeStr(DateUtil.stampToDate(Long.valueOf(time)));
		advHis.setChecksum(checksum.toLowerCase());
		advHis.setCreateDate(DateUtil.getCurrentLongDateTime());
		advHis.setActiveNum(active_num);
		advHis.setResult(flag?"校验正确":"校验失败");
		advHis.setResultSum(md5Result);
		boolean flag2 = this.advHisService.addAdvHis(advHis);
		if(!flag2) {
			logger.warn("回调记录保存失败");
		}
		if(flag) {	
			logger.info("数据校验正确");
			return "{\"message\":\"成功\",\"success\":\"true\"}";
		}else {
			logger.warn("数据校验失败。md5Result=" + md5Result 
					+ ", checksum=" + checksum.toLowerCase());
			return "{\"message\":\"数据校验失败\",\"success\":\"false\"}";
		}
	}
	
	/**
	 * 新增平台SDK广告记录
	 */
	@RequestMapping("/doSdkTasklist")
	public JsonResult doSdkTasklist(String phoneId, String adid,String cid,
			String intro,String url,String icon,String psize,String title,
			String text1,String text2,String android_url,String active_time,
			String runtime,String curr_note,String active_num,String score) {
		UsersVo users = this.usersService.selectUsersByPhoneId(phoneId);
		if(users != null) {
			SdkTasklistVo sdkTasklistVo = new SdkTasklistVo();
			sdkTasklistVo.setAdid(adid);
			sdkTasklistVo.setCid(cid);
			sdkTasklistVo.setIntro(intro);
			sdkTasklistVo.setUrl(url);
			sdkTasklistVo.setIcon(icon);
			sdkTasklistVo.setPsize(psize);
			sdkTasklistVo.setTitle(title);
			sdkTasklistVo.setText1(text1);
			sdkTasklistVo.setText2(text2);
			sdkTasklistVo.setAndroid_url(android_url);
			sdkTasklistVo.setActive_time(active_time);
			sdkTasklistVo.setRuntime(runtime);
			sdkTasklistVo.setCurr_note(curr_note);
			sdkTasklistVo.setActive_num(active_num);
			sdkTasklistVo.setScore(score);
			String cTime = DateUtil.getCurrentLongDateTime();
			sdkTasklistVo.setCreateDate(cTime);
			sdkTasklistVo.setUpdateDate(cTime);
			return this.sdkTasklistService.addSdkTasklist(sdkTasklistVo);
		}else {
			return new JsonResult(ResultCode.SUCCESS_NO_USER);
		}
	}
	
	/**
	 * 用户反馈信息接口
	 */
	@RequestMapping("/doFeedback")
	public JsonResult doFeedback(String phoneId, String info) {
		if(!StrUtil.null2Str(phoneId).equals("") && !StrUtil.null2Str(info).equals("")) {
			UsersVo users = this.usersService.selectUsersByPhoneId(phoneId);
			if(users != null) {
				UserFeedbackVo userFeedbackVo = new UserFeedbackVo();
				userFeedbackVo.setId(UUID.getUUID());
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
				return new JsonResult(ResultCode.SUCCESS_NO_USER);
			}
		}else {
			return new JsonResult(ResultCode.PARAMS_ERROR);
		}
	}
	
	/**
	 * rest推送测试用接口
	 * 说明：
	 * phoneId，手机IMEI值
	 * type，消息类型（1=通知栏消息，2=透传消息）
	 */
	@RequestMapping("/testPushNotify")
	public String testPushNotify(String phoneId, int type) {
		return this.userTaskListService.testPushNotify(phoneId, type);
	}
	
	/**
	 * 短信验证码接口
	 */
	@RequestMapping("/doSms")
	public JsonResult doSms(String phoneId, String phoneNum, String msgCode) {
		boolean result = SmsService.sendSms(phoneNum, msgCode);;
		if(result) {
			return new JsonResult(ResultCode.SUCCESS);
		}else{
			return new JsonResult(ResultCode.SUCCESS_FAIL);
		}
	}
}