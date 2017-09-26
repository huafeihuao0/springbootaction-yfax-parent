package com.yfax.webapi;

import java.text.DecimalFormat;
import java.util.Random;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GlobalUtils {
	/**
	 * 统一访问前缀：/yfax-ytt-api
	 */
	public static final String URL = "/yfax-ytt-api";
	/**
	 * 全民头条api接口：/api/ytt
	 */
	public static final String PROJECT_YTT = "/api/ytt";
	/**
	 * 格式化
	 */
	public static final String DECIMAL_FORMAT = "#0.000";
	/**
	 * 随机奖励
	 */
//	public static final int[] RANDOM_GOLD = new int[] {20,21,22,23,24,25,26,27,28,29,30};
	/**
	 * 首次有效阅读
	 */
	public static final int AWARD_TYPE_FIRSTREAD = 1;
	/**
	 * 邀请奖励
	 */
	public static final int AWARD_TYPE_INVITE = 2;
	/**
	 * 每日签到
	 */
	public static final int AWARD_TYPE_DAYLY = 3;
	/**
	 * 阅读奖励
	 */
	public static final int AWARD_TYPE_READ = 4;
	/**
	 * 徒弟贡献奖励
	 */
	public static final int AWARD_TYPE_STUDENT = 5;
	/**
	 * 首次分享奖励
	 */
	public static final int AWARD_TYPE_FIRSTSHARE = 6;
	/**
	 * 首次邀请奖励
	 */
	public static final int AWARD_TYPE_FIRSTINVITE = 7;
	/**
	 * 注册奖励
	 */
	public static final int AWARD_TYPE_REGISTER = 9;
	/**
	 * 时段奖励
	 */
	public static final int AWARD_TYPE_TIME = 10;
	/**
	 * 兑换类型-兑换金币
	 */
	public static final int BALANCE_TYPE_REDEEM = 1;
	/**
	 * 兑换类型-提现申请
	 */
	public static final int BALANCE_TYPE_WITHDRAW = 1;
	/**
	 * 短信模板ID
	 */
	public static final String SMS_TEMPLATE_ID = "206740";
	/**
	 * 短信模板ID
	 */
	public static final String SMS_APP_ID = "8a216da85ea31fdd015ea378f48c005e";
	
	/**
	 * 取奖励类型名
	 * @param award_type
	 * @return
	 */
	public static String getAwardTypeName(int awardType) {
		switch (awardType) {
			case AWARD_TYPE_FIRSTREAD:
				return "首次有效阅读";
			case AWARD_TYPE_INVITE:
				return "邀请奖励";
			case AWARD_TYPE_DAYLY:
				return "每日签到";
			case AWARD_TYPE_READ:
				return "阅读奖励";
			case AWARD_TYPE_STUDENT:
				return "徒弟贡献奖励";
			case AWARD_TYPE_FIRSTSHARE:
				return "首次分享奖励";
			case AWARD_TYPE_FIRSTINVITE:
				return "首次邀请奖励";
			case AWARD_TYPE_REGISTER:
				return "注册奖励";
			case AWARD_TYPE_TIME:
				return "时段奖励";
		}
		return "未知奖励类型";
	}
	
	/**
	 * 根据金币，返回随机金币值
	 * @param goldRange
	 */
	public static int getRanomGold(String goldRange) {
		String[] strings = goldRange.split("#");
		int start = Integer.valueOf(strings[0]);
		int end = Integer.valueOf(strings[1]);
		int count = 0;
		for (int i = start; i <= end; i++) {
			count++;
		}
		int[] golds = new int[count];
		for (int i = 0; i < golds.length; i++) {
			golds[i] = start + i;
		}
		return golds[new Random().nextInt(golds.length)];
	}
	
	/**
	 * 信鸽推送ACCESS_ID
	 */
	public final static long XG_ACCESS_ID = 2100266505;
	
	/**
	 * 信鸽推送SECRET_KEY
	 */
	public final static String XG_SECRET_KEY = "3a4cbec8bad30976f0f4bee732c6c988";
	
	/**
	 * 获得连续签到阀值的金币值
	 * @param gold 随机金币
	 * @param userGold 金币余额
	 * @param checkInConfig 金币阀值配置
	 * @return
	 */
	public static int getContinueCheckInGold(int gold, int userGold, String checkInConfig) {
		//格式化，保留三位小数，四舍五入
		DecimalFormat dFormat = new DecimalFormat("#0"); 
		int result = 0;
		JSONArray jsonArray = JSONArray.fromObject(checkInConfig);
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			String alpha = jsonObj.get("alpha").toString();
			String goldRange = jsonObj.get("goldRange").toString();
			String[] strings = goldRange.split("#");
			int start = Integer.valueOf(strings[0]);
			int end = Integer.valueOf(strings[1]);
			if(userGold>start && userGold<=end) {
				result = Integer.valueOf(dFormat.format(gold * Double.valueOf(alpha)));
				break;
			}
		}
		return result;
	}
}
