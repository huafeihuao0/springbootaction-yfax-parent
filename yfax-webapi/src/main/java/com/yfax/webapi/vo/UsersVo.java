package com.yfax.webapi.vo;

import java.io.Serializable;

/**
 * 手机用户IM表
 * @author Minbo
 */
public class UsersVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String phoneId;
	private String balance;
	private String createDate;
	private String updateDate;
	private String totalncome;
	private String todayIncome;
	private String currentTime;
	
	public String getTotalncome() {
		return totalncome;
	}
	public void setTotalncome(String totalncome) {
		this.totalncome = totalncome;
	}
	public String getTodayIncome() {
		return todayIncome;
	}
	public void setTodayIncome(String todayIncome) {
		this.todayIncome = todayIncome;
	}
	public String getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}
	public String getPhoneId() {
		return phoneId;
	}
	public void setPhoneId(String phoneId) {
		this.phoneId = phoneId;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
}
