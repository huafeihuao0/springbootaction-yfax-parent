package com.yfax.webapi.rest;

import java.util.HashMap;
import java.util.Map;
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
	public JsonResult doLogin(String phoneId) {
		if(!StrUtil.null2Str(phoneId).equals("")) {
			if(phoneId.length() == 15) {
				UsersVo users = this.usersService.selectUsersByPhoneId(phoneId);
				if(users == null) {
					boolean result = this.usersService.addUser(phoneId);
					if(!result) {
						return new JsonResult(ResultCode.EXCEPTION, "发生异常", null);
					}
				}
				//TODO 返回一个tokenId值
				Map<String,String> map = new HashMap<String, String>();
				String tokenId = UUID.getUUID();
				map.put("tokenId", tokenId);
				return new JsonResult(ResultCode.SUCCESS, "成功", map);
			}else {
				return new JsonResult(ResultCode.IMEI_ERROR, "IMEI值错误", null);
			}
			
		}else {
			return new JsonResult(ResultCode.PARAMS_ERROR, "参数错误", null);
		}
	}
	
	/**
	 * 用户抢购任务接口
	 */
	@RequestMapping("/doPanicBuying")
	public JsonResult doPanicBuying(String phoneId, String taskId) {
		if(!StrUtil.null2Str(phoneId).equals("")) {
			UsersVo users = this.usersService.selectUsersByPhoneId(phoneId);
			if(users != null) {
				return this.userTaskListService.doPanicBuying(phoneId, taskId);
			}else {
				return new JsonResult(ResultCode.SUCCESS_NO_USER, "用户不存在", null);
			}
		}else {
			return new JsonResult(ResultCode.PARAMS_ERROR, "参数错误", null);
		}
		
	}
	
	/**
	 * 用户放弃任务接口
	 */
	@RequestMapping("/doAbandonTask")
	public JsonResult doAbandonTask(String phoneId, String taskId) {
		if(!StrUtil.null2Str(phoneId).equals("")) {
			UsersVo users = this.usersService.selectUsersByPhoneId(phoneId);
			if(users != null) {
				return this.userTaskListService.abandonUserTask(phoneId, taskId);
			}else {
				return new JsonResult(ResultCode.SUCCESS_NO_USER, "用户不存在", null);
			}
		}else {
			return new JsonResult(ResultCode.PARAMS_ERROR, "参数错误", null);
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
				return new JsonResult(ResultCode.SUCCESS_NO_USER, "用户不存在", null);
			}
		}else {
			return new JsonResult(ResultCode.PARAMS_ERROR, "参数错误", null);
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
					return new JsonResult(ResultCode.SUCCESS_NOT_ENOUGH, "失败，余额不足", null);
				}
			}else {
				return new JsonResult(ResultCode.SUCCESS_NO_USER, "用户不存在", null);
			}
		}else {
			return new JsonResult(ResultCode.PARAMS_ERROR, "参数错误", null);
		}
	}
	
	/**
	 * 用户已点击任务查看记录接口
	 */
	@RequestMapping("/doCheck")
	public JsonResult doCheck(String phoneId, String id) {
		if(!StrUtil.null2Str(phoneId).equals("")) {
			UsersVo users = this.usersService.selectUsersByPhoneId(phoneId);
			if(users != null) {
				boolean result = this.userTaskListService.setIsChecked(id);
				if(result) {
					return new JsonResult(ResultCode.SUCCESS, "成功", null);
				}else{
					return new JsonResult(ResultCode.SUCCESS_NO_DATA, "数据为空", null);
				}
			}else {
				return new JsonResult(ResultCode.SUCCESS_NO_USER, "用户不存在", null);
			}
		}else {
			return new JsonResult(ResultCode.PARAMS_ERROR, "参数错误", null);
		}
	}
}