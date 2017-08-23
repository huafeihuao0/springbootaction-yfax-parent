package com.yfax.webapi;

public class GlobalUtils {
	/**
	 * 统一访问前缀：/yfax-qmtt-api
	 */
	public static final String URL = "/yfax-qmtt-api";
	/**
	 * 全民头条api接口：/api/qmtt
	 */
	public static final String PROJECT_QMTT = "/api/qmtt";
	/**
	 * 格式化
	 */
	public static final String DECIMAL_FORMAT = "#0.000";
	/**
	 * 随机奖励
	 */
	public static final int[] RANDOM_GOLD = new int[] {20,21,22,23,24,25,26,27,28,29,30};
	/**
	 * 首次有效阅读
	 */
	public static final int AWARD_TYPE_FIRSTREAD = 1;
	/**
	 * 首次有效阅读奖励金币
	 */
	public static final int AWARD_TYPE_FIRSTREAD_GOLD = 300;
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
	 * 首次分享奖励金币
	 */
	public static final int AWARD_TYPE_FIRSTSHARE_GOLD = 500;
	/**
	 * 首次邀请奖励
	 */
	public static final int AWARD_TYPE_FIRSTINVITE = 7;
	/**
	 * 首次邀请奖励金币
	 */
	public static final int AWARD_TYPE_FIRSTINVITE_GOLD = 3000;
	/**
	 * 兑换类型-兑换金币
	 */
	public static final int BALANCE_TYPE_REDEEM = 1;
	
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
		}
		return "未知奖励类型";
	}
}
