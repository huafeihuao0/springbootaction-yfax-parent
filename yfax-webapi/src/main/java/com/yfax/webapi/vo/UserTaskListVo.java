package com.yfax.webapi.vo;

import java.io.Serializable;

/**
 * 用户任务表
 * @author Minbo
 */
public class UserTaskListVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String phoneId;
	private String taskId;
	private String logoUrl;
	private String taskName;
	private String taskTag;
	private int amount;
	private String income;
	private int orderNo;
	private String goUrl;
	private String status;
	private int statusFlag;
	private int isChecked;
	private String createDate;
	private String updateDate;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPhoneId() {
		return phoneId;
	}
	public void setPhoneId(String phoneId) {
		this.phoneId = phoneId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getTaskTag() {
		return taskTag;
	}
	public void setTaskTag(String taskTag) {
		this.taskTag = taskTag;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getIncome() {
		return income;
	}
	public void setIncome(String income) {
		this.income = income;
	}
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public String getGoUrl() {
		return goUrl;
	}
	public void setGoUrl(String goUrl) {
		this.goUrl = goUrl;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getStatusFlag() {
		return statusFlag;
	}
	public void setStatusFlag(int statusFlag) {
		this.statusFlag = statusFlag;
	}
	public int getIsChecked() {
		return isChecked;
	}
	public void setIsChecked(int isChecked) {
		this.isChecked = isChecked;
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
