package com.yfax.webapi.qmtt.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yfax.utils.JsonResult;
import com.yfax.utils.ResultCode;
import com.yfax.webapi.qmtt.service.AppUserService;
import com.yfax.webapi.qmtt.service.AwardHisService;
import com.yfax.webapi.qmtt.service.BalanceHisService;
import com.yfax.webapi.qmtt.service.IncomeSetService;
import com.yfax.webapi.qmtt.service.ReadHisService;
import com.yfax.webapi.qmtt.service.WithdrawHisService;
import com.yfax.webapi.qmtt.vo.AppUserVo;
import com.yfax.webapi.qmtt.vo.AwardHisVo;
import com.yfax.webapi.qmtt.vo.BalanceHisVo;
import com.yfax.webapi.qmtt.vo.IncomeSetVo;
import com.yfax.webapi.qmtt.vo.ReadHisVo;
import com.yfax.webapi.qmtt.vo.WithdrawHisVo;

/**
 * @author Minbo.He 
 * 查询接口
 */
@RestController
@RequestMapping("/api/qmtt")
public class AppQueryRest {

	protected static Logger logger = LoggerFactory.getLogger(AppQueryRest.class);

	@Autowired
	private AwardHisService awardHisService;
	@Autowired
	private BalanceHisService balanceHisService;
	@Autowired
	private IncomeSetService incomeSetService;
	@Autowired
	private AppUserService appUserService;
	@Autowired
	private WithdrawHisService withdrawHisService;
	@Autowired
	private ReadHisService readHisService;
	
	/**
	 * 个人资产接口
	 */
	@RequestMapping("/queryOwnInfo")
	public JsonResult queryOwnInfo(String phoneNum) {
		return new JsonResult(ResultCode.SUCCESS);
	}
	
	/**
	 * 金币奖励记录接口
	 */
	@RequestMapping("/queryAwardHis")
	public JsonResult queryAwardHis(String phoneNum) {
		List<AwardHisVo> list = this.awardHisService.selectAwardHisByPhoneNum(phoneNum);
		return new JsonResult(ResultCode.SUCCESS, list);
	}
	
	/**
	 * 零钱兑换记录接口
	 */
	@RequestMapping("/queryBalanceHis")
	public JsonResult queryBalanceHis(String phoneNum) {
		List<BalanceHisVo> list = this.balanceHisService.selectBalanceHisByPhoneNum(phoneNum);
		return new JsonResult(ResultCode.SUCCESS, list);
	}
	
	/**
	 * 提现配置接口
	 */
	@RequestMapping("/queryIncomeSet")
	public JsonResult queryIncomeSet() {
		List<IncomeSetVo> list = this.incomeSetService.selectIncomeSet();
		return new JsonResult(ResultCode.SUCCESS, list);
	}
	
	/**
	 * 兑换提现记录接口
	 */
	@RequestMapping("/queryWithdrawHis")
	public JsonResult queryWithdrawHis(String phoneNum) {
		AppUserVo appUserVo = this.appUserService.selectByPhoneNum(phoneNum);
		if (appUserVo != null) {
			List<WithdrawHisVo> withdrawHis = this.withdrawHisService.selectWithdrawHis(appUserVo.getPhoneNum());
			return new JsonResult(ResultCode.SUCCESS, withdrawHis);
		} else {
			return new JsonResult(ResultCode.SUCCESS_NO_USER);
		}
	}
	
	/**
	 * 获得阅读文章历史
	 */
	@RequestMapping("/queryReadHis")
	public JsonResult queryReadHis(String phoneNum) {
		List<ReadHisVo> list = this.readHisService.selectReadHisByPhoneNum(phoneNum);
		return new JsonResult(ResultCode.SUCCESS, list);
	}
}