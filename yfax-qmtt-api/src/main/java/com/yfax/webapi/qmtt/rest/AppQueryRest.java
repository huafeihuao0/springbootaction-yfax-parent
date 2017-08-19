package com.yfax.webapi.qmtt.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yfax.utils.JsonResult;
import com.yfax.utils.ResultCode;
import com.yfax.webapi.qmtt.service.AwardHisService;
import com.yfax.webapi.qmtt.service.BalanceHisService;
import com.yfax.webapi.qmtt.vo.AwardHisVo;
import com.yfax.webapi.qmtt.vo.BalanceHisVo;

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
}