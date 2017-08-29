package com.yfax.webapi.qmtt.rest;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yfax.utils.JsonResult;
import com.yfax.utils.ResultCode;
import com.yfax.webapi.GlobalUtils;
import com.yfax.webapi.qmtt.service.AppConfigService;
import com.yfax.webapi.qmtt.service.AppUserService;
import com.yfax.webapi.qmtt.service.AwardHisService;
import com.yfax.webapi.qmtt.service.BalanceHisService;
import com.yfax.webapi.qmtt.service.IncomeSetService;
import com.yfax.webapi.qmtt.service.InitConfigService;
import com.yfax.webapi.qmtt.service.RateSetService;
import com.yfax.webapi.qmtt.service.ReadHisService;
import com.yfax.webapi.qmtt.service.WithdrawHisService;
import com.yfax.webapi.qmtt.vo.AppConfigVo;
import com.yfax.webapi.qmtt.vo.AppUserVo;
import com.yfax.webapi.qmtt.vo.AwardHisVo;
import com.yfax.webapi.qmtt.vo.BalanceHisVo;
import com.yfax.webapi.qmtt.vo.IncomeSetVo;
import com.yfax.webapi.qmtt.vo.InitConfigVo;
import com.yfax.webapi.qmtt.vo.RateSetVo;
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
	@Autowired
	private RateSetService rateSetService;
	@Autowired
	private AppConfigService appConfigService;
	@Autowired
	private InitConfigService initConfigService;
	
	/**
	 * 个人信息接口
	 */
	@RequestMapping("/queryOwnInfo")
	public JsonResult queryOwnInfo(String phoneNum) {
		Map<String, Object> map = new HashMap<>();
		//我的零钱，我的金币
		AppUserVo appUserVo = this.appUserService.selectByPhoneNum(phoneNum);
		map.put("gold", appUserVo.getGold());
		map.put("balance", appUserVo.getBalance());
		//得到当前汇率
		RateSetVo rateSetVo = this.rateSetService.selectRateSet();
		map.put("rate", rateSetVo.getRate());
		map.put("students", appUserVo.getStudents());
		map.put("appUserVo", appUserVo);
		//配置信息
		AppConfigVo appConfigVo = this.appConfigService.selectAppConfig();
		map.put("appConfigVo", appConfigVo);
		return new JsonResult(ResultCode.SUCCESS, map);
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
	
	/**
	 * 获得排行榜数据
	 */
	@RequestMapping("/queryRank")
	public JsonResult queryRank() {
		Map<String, Object> map = new HashMap<>();
		List<AppUserVo> list = this.appUserService.selectByRank();
		if(list.size()>0) {
			map.put("list", list);
			Long sum = this.appUserService.selectByRankSum();
			//格式化，保留三位小数，DB做四舍五入
			DecimalFormat dFormat = new DecimalFormat(GlobalUtils.DECIMAL_FORMAT); 
			map.put("sum", dFormat.format(sum));
		}else {
			map.put("list", null);
			map.put("sum", "0.000");
		}
		return new JsonResult(ResultCode.SUCCESS, map);
	}
	
	/**
	 * 获得APP初始化配置数据
	 */
	@RequestMapping("/queryInitConfig")
	public JsonResult queryInitConfig() {
		InitConfigVo initConfigVo = this.initConfigService.selectInitConfig();
		return new JsonResult(ResultCode.SUCCESS, initConfigVo);
	}
	
}