package com.yfax.webapi.rest;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yfax.webapi.service.UserTaskListService;
import com.yfax.webapi.service.UsersService;
import com.yfax.webapi.service.WithdrawHisService;
import com.yfax.webapi.utils.JsonResult;
import com.yfax.webapi.utils.ResultCode;
import com.yfax.webapi.utils.StrUtil;
import com.yfax.webapi.utils.UUID;
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
}