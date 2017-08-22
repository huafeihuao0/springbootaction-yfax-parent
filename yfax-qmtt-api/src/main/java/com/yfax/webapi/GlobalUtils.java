package com.yfax.webapi;

public interface GlobalUtils {
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
	 * 新手奖励
	 */
	public static final int AWARD_TYPE_NEW = 1;
	/**
	 * 邀请奖励
	 */
	public static final int AWARD_TYPE_SHARE = 2;
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
	 * 兑换类型-兑换金币
	 */
	public static final int BALANCE_TYPE_REDEEM = 1;
}
