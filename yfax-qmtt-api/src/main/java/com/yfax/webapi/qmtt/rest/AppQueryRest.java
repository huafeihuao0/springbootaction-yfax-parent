package com.yfax.webapi.qmtt.rest;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
import com.yfax.webapi.qmtt.service.AppVersionService;
import com.yfax.webapi.qmtt.service.AwardHisService;
import com.yfax.webapi.qmtt.service.BalanceHisService;
import com.yfax.webapi.qmtt.service.IncomeSetService;
import com.yfax.webapi.qmtt.service.InitConfigService;
import com.yfax.webapi.qmtt.service.RateSetService;
import com.yfax.webapi.qmtt.service.ReadHisService;
import com.yfax.webapi.qmtt.service.WithdrawHisService;
import com.yfax.webapi.qmtt.vo.AppConfigVo;
import com.yfax.webapi.qmtt.vo.AppUserVo;
import com.yfax.webapi.qmtt.vo.AppVersionVo;
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
	@Autowired
	private AppVersionService appVersionService;
	
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
			List<AppUserVo> listTmp = initTestData(list);	//for test
			map.put("list", listTmp);	//for test
			map.put("sum", calSum(listTmp));		//for test
			
//			map.put("list", list);
//			Long sum = this.appUserService.selectByRankSum();
			//格式化，保留三位小数，DB做四舍五入
//			DecimalFormat dFormat = new DecimalFormat(GlobalUtils.DECIMAL_FORMAT); 
//			map.put("sum", dFormat.format(sum));
		}else {
//			map.put("list", null);
//			map.put("sum", "0.000");
			
			List<AppUserVo> listTmp = initTestData(list);	//for test
			map.put("list", listTmp);	//for test
			map.put("sum", calSum(listTmp));		//for test
		}
		return new JsonResult(ResultCode.SUCCESS, map);
	}
	
	//测试方法，统计总收益金额
	private String calSum(List<AppUserVo> list) {
		double sum = 0;
		for (AppUserVo appUserVo : list) {
			sum += Double.valueOf(appUserVo.getBalance());
		}
		//格式化，保留三位小数，DB做四舍五入
		DecimalFormat dFormat = new DecimalFormat(GlobalUtils.DECIMAL_FORMAT); 
		return dFormat.format(sum);
	}
	
	//测试方法，虚拟数据
	private List<AppUserVo> initTestData(List<AppUserVo> list) {
		if(list == null) {
			list = new ArrayList<>();
		}
		String[] strings = new String[] {"15900008732","13300008123","18400001351","13500002314","15600006532","15800001317"
				,"13000004123","18000009873","18800009345","18200001713","13900001532","15900002017","13300001789","18400001638"
				,"13500002325","15600006843","15800001335","13000008731","18000007232","18800002545","18200006813","13900001314"
				,"15900005632","13300008683","18400001332","13500002338","15600006682","15800001334","13000004153","18000009773"
				,"18800009377","18200001733","13900001632"};
		//格式化，保留三位小数，DB做四舍五入
		DecimalFormat dFormat = new DecimalFormat(GlobalUtils.DECIMAL_FORMAT); 
		for (int i = 0; i < strings.length; i++) {
			AppUserVo appUserVo = new AppUserVo();
			appUserVo.setPhoneNum(strings[i]);
			appUserVo.setBalance(String.valueOf(dFormat.format(new Random().nextInt(100) * new Random().nextFloat())));
			list.add(appUserVo);
		}
		Collections.sort(list, new Comparator<AppUserVo>() {
	        @Override
	        public int compare(AppUserVo o1, AppUserVo o2) {
	        		String str = dFormat.format(Double.valueOf(o2.getBalance()) - Double.valueOf(o1.getBalance()));
	        		DecimalFormat myFormat = new DecimalFormat("#0");
	        		return Integer.valueOf(myFormat.format(Double.valueOf(str)));
	        }
	    });
		return list.subList(0, 20);
	}
	/**
	 * 获得APP初始化配置数据
	 */
	@RequestMapping("/queryInitConfig")
	public JsonResult queryInitConfig() {
		//1. 初始配置数据
		InitConfigVo initConfigVo = this.initConfigService.selectInitConfig();
		//2. APP渠道版本配置
		AppVersionVo appVersionVo = this.appVersionService.selectAppVersion();
		initConfigVo.setAppVersionVo(appVersionVo);
		return new JsonResult(ResultCode.SUCCESS, initConfigVo);
	}
	
}