package com.yfax.webapi.qmtt.rest;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yfax.utils.DateUtil;
import com.yfax.utils.JsonResult;
import com.yfax.utils.ResultCode;
import com.yfax.utils.StrUtil;
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
		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("phoneNum", phoneNum);
		paramsMap.put("currentTime", DateUtil.getCurrentDate());
		//今日已获得金币
		Long todayGlod = this.appUserService.selectByTodaySum(paramsMap);
		Map<String, Object> resultMap = new HashMap<>();
		//我的零钱，我的金币
		AppUserVo appUserVo = this.appUserService.selectByPhoneNum(phoneNum);
		resultMap.put("gold", appUserVo.getGold());
		resultMap.put("balance", appUserVo.getBalance());
		resultMap.put("todayGlod", StrUtil.null2Str(String.valueOf(todayGlod)));
		//徒弟名下贡献奖励金币数
		Long totalGold = this.appUserService.selectByTotalGold(paramsMap);
		resultMap.put("totalGold", totalGold);
		//得到当前汇率
		RateSetVo rateSetVo = this.rateSetService.selectRateSet();
		resultMap.put("rate", rateSetVo.getRate());
		resultMap.put("students", appUserVo.getStudents());
		resultMap.put("appUserVo", appUserVo);
		//配置信息
		AppConfigVo appConfigVo = this.appConfigService.selectAppConfig();
		resultMap.put("appConfigVo", appConfigVo);
		return new JsonResult(ResultCode.SUCCESS, resultMap);
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
	
	/**
	 * 获得排行榜数据-金币排行榜
	 */
	@RequestMapping("/queryRankGold")
	public JsonResult queryRankGold() {
		Map<String, Object> map = new HashMap<>();
		List<AppUserVo> list = new ArrayList<>();	//this.appUserService.selectByRankGold();
		if(list.size()>0) {
			List<AppUserVo> listTmp = initTestDataGold(list);		//for test
			map.put("list", listTmp);			//for test
			map.put("sum", "0");		//for test
			
//			map.put("list", list);
//			Long sum = this.appUserService.selectByRankSum();
			//格式化，保留三位小数，DB做四舍五入
//			DecimalFormat dFormat = new DecimalFormat(GlobalUtils.DECIMAL_FORMAT); 
//			map.put("sum", dFormat.format(sum));
		}else {
//			map.put("list", null);
//			map.put("sum", "0.000");
			
			List<AppUserVo> listTmp = initTestDataGold(list);	//for test
			map.put("list", listTmp);	//for test
			map.put("sum", "0");		//for test
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
	
//	public static void main(String[] args) {
//		AppQueryRest rest = new AppQueryRest();
//		List<AppUserVo> list = rest.initTestDataGold(new ArrayList<AppUserVo>());
//		for (AppUserVo appUserVo : list) {
//			System.out.println(appUserVo);
//		}
//	}
	
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
		int salt = 10;
		//格式化，保留三位小数，DB做四舍五入
		DecimalFormat dFormat = new DecimalFormat(GlobalUtils.DECIMAL_FORMAT); 
		for (int i = 0; i < 3; i++) {
			AppUserVo appUserVo = new AppUserVo();
			appUserVo.setPhoneNum(strings[i]);
			appUserVo.setBalance(String.valueOf(dFormat.format(new Random().nextInt(100) * salt * new Random().nextFloat())));
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
	
	//测试方法，虚拟数据-金币数
	private List<AppUserVo> initTestDataGold(List<AppUserVo> list) {
		if(list == null) {
			list = new ArrayList<>();
		}
		String[] strings = new String[] {"15900008732","13300008123","18400001351","13500002314","15600006532","15800001317"
				,"13000004123","18000009873","18800009345","18200001713","13900001532","15900002017","13300001789","18400001638"
				,"13500002325","15600006843","15800001335","13000008731","18000007232","18800002545","18200006813","13900001314"
				,"15900005632","13300008683","18400001332","13500002338","15600006682","15800001334","13000004153","18000009773"
				,"18800009377","18200001733","13900001632"};
		for (int i = 0; i < 3; i++) {
			AppUserVo appUserVo = new AppUserVo();
			appUserVo.setPhoneNum(strings[i]);
			appUserVo.setGold(String.valueOf(getValue(1)));
			list.add(appUserVo);
		}
		for (int i = 3; i < 11; i++) {
			AppUserVo appUserVo = new AppUserVo();
			appUserVo.setPhoneNum(strings[i]);
			appUserVo.setGold(String.valueOf(getValue(2)));
			list.add(appUserVo);
		}
		for (int i = 11; i < 21; i++) {
			AppUserVo appUserVo = new AppUserVo();
			appUserVo.setPhoneNum(strings[i]);
			appUserVo.setGold(String.valueOf(getValue(3)));
			list.add(appUserVo);
		}
		Collections.sort(list, new Comparator<AppUserVo>() {
	        @Override
	        public int compare(AppUserVo o1, AppUserVo o2) {
	        		int result = Integer.valueOf(o2.getGold()) - Integer.valueOf(o1.getGold());
	        		return result;
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
	
//	public static void main(String[] args) {
//		
//		DecimalFormat myFormat = new DecimalFormat("#0");
//		System.out.println("1=" + getValue(1));
//		System.out.println("2=" + getValue(1));
//		System.out.println("3=" + getValue(1));
//		System.out.println();
//		System.out.println("4=" + getValue(2));
//		System.out.println("5=" + getValue(2));
//		System.out.println("6=" + getValue(2));
//		System.out.println("7=" + getValue(2));
//		System.out.println("8=" + getValue(2));
//		System.out.println("9=" + getValue(2));
//		System.out.println("10=" + getValue(2));
//		System.out.println();
//		System.out.println("11=" + getValue(3));
//		System.out.println("12=" + getValue(3));
//		System.out.println("13=" + getValue(3));
//		System.out.println("14=" + getValue(3));
//		System.out.println("15=" + getValue(3));
//		System.out.println("16=" + getValue(3));
//		System.out.println("17=" + getValue(3));
//		System.out.println("18=" + getValue(3));
//		System.out.println("19=" + getValue(3));
//		System.out.println("20=" + getValue(3));
//		
//		System.exit(1);
//		
//		List<AppUserVo> list = new ArrayList<>();
//		
//		String[] strings = new String[] {"15900008732","13300008123","18400001351","13500002314","15600006532","15800001317"
//				,"13000004123","18000009873","18800009345","18200001713","13900001532","15900002017","13300001789","18400001638"
//				,"13500002325","15600006843","15800001335","13000008731","18000007232","18800002545","18200006813","13900001314"
//				,"15900005632","13300008683","18400001332","13500002338","15600006682","15800001334","13000004153","18000009773"
//				,"18800009377","18200001733","13900001632"};
//		for (int i = 0; i < 3; i++) {
//			AppUserVo appUserVo = new AppUserVo();
//			appUserVo.setPhoneNum(strings[i]);
////			DecimalFormat myFormat = new DecimalFormat("#0");
//			appUserVo.setGold(myFormat.format(Double.valueOf(new Random().nextDouble() * 100000000 * new Random().nextFloat())));
//			list.add(appUserVo);
//		}
////		for (int i = 3; i < 11; i++) {
////			AppUserVo appUserVo = new AppUserVo();
////			appUserVo.setPhoneNum(strings[i]);
////			DecimalFormat myFormat = new DecimalFormat("#0");
////			appUserVo.setGold(myFormat.format(Double.valueOf(new Random().nextFloat() * 1000000 * new Random().nextFloat())));
////			list.add(appUserVo);
////		}
////		for (int i = 11; i < strings.length; i++) {
////			AppUserVo appUserVo = new AppUserVo();
////			appUserVo.setPhoneNum(strings[i]);
////			DecimalFormat myFormat = new DecimalFormat("#0");
////			appUserVo.setGold(myFormat.format(Double.valueOf(new Random().nextFloat() * 1000000 * new Random().nextFloat())));
////			list.add(appUserVo);
////		}
//		Collections.sort(list, new Comparator<AppUserVo>() {
//	        @Override
//	        public int compare(AppUserVo o1, AppUserVo o2) {
//	        		int result = Integer.valueOf(o2.getGold()) - Integer.valueOf(o1.getGold());
//	        		return result;
//	        }
//	    });
//		
//		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
//			AppUserVo appUserVo = (AppUserVo) iterator.next();
//			System.out.println(appUserVo.toString());
//		}
//	}
	
	public static Integer getValue(int type) {
		DecimalFormat myFormat = new DecimalFormat("#0");
		Integer result = 0;
		if(type == 1) {
			while(true) {
				result = Integer.valueOf(myFormat.format(new Random().nextFloat() * 100000000 * new Random().nextFloat())) * 2;
				if(result>5000*10000 && result<10000*10000) {
					return result;
				}
			}
		}else if(type == 2) {
			while(true) {
				result = Integer.valueOf(myFormat.format(new Random().nextFloat() * 100000000 * new Random().nextFloat()));
				if(result>2000*10000 && result<5000*10000) {
					return result;
				}
			}
		}else if(type == 3) {
			while(true) {
				result = Integer.valueOf(myFormat.format(new Random().nextFloat() * 100000000 * new Random().nextFloat()));
				if(result>500*10000 && result<2000*10000) {
					return result;
				}
			}
		}
		return result;
	}
}