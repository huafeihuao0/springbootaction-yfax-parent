package com.yfax.webapi.rest;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yfax.webapi.service.AdvHisService;
import com.yfax.webapi.service.UserTaskListService;
import com.yfax.webapi.service.UsersService;
import com.yfax.webapi.service.WithdrawHisService;
import com.yfax.webapi.utils.DateUtil;
import com.yfax.webapi.utils.JsonResult;
import com.yfax.webapi.utils.MD5Util;
import com.yfax.webapi.utils.ResultCode;
import com.yfax.webapi.utils.StrUtil;
import com.yfax.webapi.utils.UUID;
import com.yfax.webapi.vo.AdvHisVo;
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
	@RequestMapping("/doProve")
	public JsonResult doProve(String phoneId, String id) {
		if(!StrUtil.null2Str(phoneId).equals("") && !StrUtil.null2Str(id).equals("")) {
			UsersVo users = this.usersService.selectUsersByPhoneId(phoneId);
			if(users != null) {
				return this.userTaskListService.doProve(phoneId, id);
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
	 * rest推送测试用接口
	 */
	@RequestMapping("/testPushNotify")
	public String testPushNotify(String phoneId) {
		return this.userTaskListService.testPushNotify(phoneId);
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
			String source,String point,String time,String appsecret,
			String checksum) {
		//1. 校验MD5数据内容
		String parameter= "?hashid=" + hashid + "&appid=" + appid + "&adid=" + adid + "&adname=" + adname + ""
				+ "&userid=" + userid + "&mac=" + mac + "&deviceid=" + deviceid + ""
				+ "&source=" + source + "&point=" + point + "&time=" + time + "&appsecret=" + AD_SECRET + "";
		logger.info("parameter=" + parameter);
		String md5Result = MD5Util.encodeByMD5(parameter)	.toLowerCase();	//小写比较	
		if(md5Result.equals(checksum.toLowerCase())) {	
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
			advHis.setTimeStr(DateUtil.stampToDate(time));
			advHis.setAppsecret(appsecret);
			advHis.setChecksum(checksum);
			advHis.setCreateDate(DateUtil.getCurrentLongDateTime());
			boolean result = this.advHisService.addAdvHis(advHis);
			if(result) {
				return "{\"message\":\"成功\",\"success\":\"true\"}";
			}else{
				logger.error("服务异常");
				return "{\"message\":\"服务异常\",\"success\":\"false\"}";
			}
		}else {
			logger.warn("数据校验失败。md5Result=" + md5Result 
					+ ", checksum=" + checksum.toLowerCase());
			return "{\"message\":\"数据校验失败\",\"success\":\"false\"}";
		}
	}
}