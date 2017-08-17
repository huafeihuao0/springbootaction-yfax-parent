package com.yfax.webapi.cfdb.rest;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

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
import com.yfax.webapi.cfdb.vo.UserFeedbackVo;
import com.yfax.webapi.cfdb.vo.UserSmsVo;
import com.yfax.webapi.cfdb.vo.UsersVo;
import com.yfax.webapi.service.UserFeedbackService;
import com.yfax.webapi.service.UserSmsService;
import com.yfax.webapi.service.UserTaskListService;
import com.yfax.webapi.service.UsersService;
import com.yfax.webapi.service.WithdrawHisService;

/**
 * @author Minbo.He
 * 冲返单包，受理接口
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
	private UserFeedbackService userFeedbackService;
	@Autowired
	private UserSmsService userSmsService;
	
	/**
	 * 用户登录接口（限定手机IM码）
	 */
	@RequestMapping(value = "/doLogin", method = {RequestMethod.POST})
	public JsonResult doLogin(String phoneId, HttpServletRequest request) {
		if(phoneId.length() == IMEI_LENGTH || phoneId.length() == 14) {
			UsersVo users = this.usersService.selectUsersByPhoneId(phoneId);
			if(users == null) {
				boolean result = this.usersService.addUser(phoneId);
				if(!result) {
					return new JsonResult(ResultCode.SUCCESS_FAIL);
				}
			}
			return new JsonResult(ResultCode.SUCCESS);
		}else {
			return new JsonResult(ResultCode.IMEI_ERROR);
		}
	}
	
	/**
	 * 用户抢购任务接口
	 */
	@RequestMapping(value = "/doPanicBuying", method = {RequestMethod.POST})
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
	@RequestMapping(value = "/doAbandonTask", method = {RequestMethod.POST})
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
	@RequestMapping(value = "/doProve", method = {RequestMethod.POST})
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
	@RequestMapping(value = "/doWithdraw", method = {RequestMethod.POST})
	public JsonResult doWithdraw(String phoneId, int withdrawType, 
			String name, String account, String income) {
		if(!StrUtil.null2Str(phoneId).equals("") && !StrUtil.null2Str(account).equals("") 
				&& !StrUtil.null2Str(income).equals("")) {
			UsersVo user = this.usersService.selectUsersByPhoneId(phoneId);
			if(user != null) {
				double incomeTmp = Double.valueOf(income);
				double balance = Double.valueOf(user.getBalance());
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
	@RequestMapping(value = "/doCheck", method = {RequestMethod.POST})
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
	 * 用户反馈信息接口
	 */
	@RequestMapping(value = "/doFeedback", method = {RequestMethod.POST})
	public JsonResult doFeedback(String phoneId, String info) {
		if(!StrUtil.null2Str(phoneId).equals("") && !StrUtil.null2Str(info).equals("")) {
			UsersVo users = this.usersService.selectUsersByPhoneId(phoneId);
			if(users != null) {
				UserFeedbackVo userFeedbackVo = new UserFeedbackVo();
				userFeedbackVo.setId(UUID.getUUID());
				userFeedbackVo.setPhoneId(phoneId);
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
	@RequestMapping(value = "/doSms", method = {RequestMethod.POST})
	public JsonResult doSms(String phoneId, String phoneNum, String msgCode) {
		if(!StrUtil.null2Str(phoneId).equals("") && !StrUtil.null2Str(phoneNum).equals("") 
				&& !StrUtil.null2Str(msgCode).equals("")) {
			UsersVo users = this.usersService.selectUsersByPhoneId(phoneId);
			if(users != null) {
				HashMap<String, Object> result = SmsService.sendSms(phoneNum, msgCode, GlobalUtils.PROJECT_CFDB);
				if("000000".equals(result.get("statusCode"))){
					UserSmsVo userSms = new UserSmsVo();
					userSms.setId(UUID.getUUID());
					userSms.setPhoneId(phoneId);
					userSms.setPhoneNum(phoneNum);
					userSms.setMsgCode(msgCode);
					userSms.setProjectCode(GlobalUtils.PROJECT_CFDB);
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
				return new JsonResult(ResultCode.SUCCESS_NO_USER);
			}
		}else {
			return new JsonResult(ResultCode.PARAMS_ERROR);
		}
	}
}