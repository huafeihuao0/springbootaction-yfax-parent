package com.yfax.webapi.vo;

import java.io.Serializable;

/**
 * 提现金额配置
 * @author Minbo
 */
public class IncomeSetVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String income;
	private String createDate;
	private String updateDate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIncome() {
		return income;
	}
	public void setIncome(String income) {
		this.income = income;
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
