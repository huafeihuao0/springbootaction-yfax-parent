package com.yfax.webapi.vo;

import java.io.Serializable;

/**
 * 任务详情
 * @author Minbo
 */
public class TaskDetailVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String taskId;
	private String taskDesc;
	private String stepUrl;
	private String steps;
	private String fields;
	private String createDate;
	private String updateDate;
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTaskDesc() {
		return taskDesc;
	}
	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}
	public String getStepUrl() {
		return stepUrl;
	}
	public void setStepUrl(String stepUrl) {
		this.stepUrl = stepUrl;
	}
	public String getSteps() {
		return steps;
	}
	public void setSteps(String steps) {
		this.steps = steps;
	}
	public String getFields() {
		return fields;
	}
	public void setFields(String fields) {
		this.fields = fields;
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
