package com.yfax.task.cfdb.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.common.xinge.XgServiceApi;
import com.yfax.task.cfdb.dao.TaskListDao;
import com.yfax.task.cfdb.dao.UserTaskListDao;
import com.yfax.task.cfdb.dao.UserTasklistHisDao;
import com.yfax.task.cfdb.vo.TaskListVo;
import com.yfax.task.cfdb.vo.UserTaskListVo;
import com.yfax.task.cfdb.vo.UserTasklistHisVo;
import com.yfax.utils.DateUtil;
import com.yfax.utils.UUID;

/**
 * 用户任务管理
 * @author Minbo
 * 1. APP每次同时只能做一个任务
 */
@Service
public class UserTaskListService {
	
	protected static Logger logger = LoggerFactory.getLogger(UserTaskListService.class);

	@Autowired
	private UserTaskListDao userTaskListDao;
	
	@Autowired
	private TaskListDao taskListDao;
	
	@Autowired
	private UserTasklistHisDao userTasklistHisDao;
	
	public List<UserTaskListVo> selectUserTaskListByPhoneId(String phoneId) {
		return this.userTaskListDao.selectUserTaskListByPhoneId(phoneId);
	}
	
	public List<UserTaskListVo> selectUserTaskListByTime() {
		return this.userTaskListDao.selectUserTaskListByTime();
	}
	

	/**
	 * 放弃进行中的任务
	 * @param phoneId
	 * @param taskId
	 * @return
	 */
	@Transactional
	public void abandonUserTask(String id, String phoneId, String taskId) {
		try {
			Map<Object, Object> map = new HashMap<>();
			map.put("phoneId", phoneId);
			map.put("taskId", taskId);
			//1. 放弃任务
			boolean flag = this.userTaskListDao.deleteUserTask(map);
			if (!flag) {
				logger.warn("数据为空");
			}
			//2. 获得任务当前剩余数量
			TaskListVo taskListVo = this.taskListDao.selectTaskListById(taskId);
			//3. 任务当前剩余数量加1
			taskListVo.setAmount(taskListVo.getAmount()+1);
			taskListVo.setUpdateDate(DateUtil.getCurrentLongDateTime());
			//4. 记录用户任务操作历史
			recordUserTaskHis(phoneId, taskListVo, 5);	//已放弃
			boolean flag1 = this.taskListDao.updateTaskListById(taskListVo);
			if (flag1) {
				logger.info("放弃成功。id=" + id + ", phoneId=" + phoneId + ", taskId=" + taskId);
			}else {
				logger.error("放弃失败。id=" + id + ", phoneId=" + phoneId + ", taskId=" + taskId);
			}
		} catch (Exception e) {
			logger.error("放弃进行中的任务异常：" + e.getMessage(), e);
		}
	}
	
	/**
	 * 记录用户任务操作历史
	 * @throws Exception 
	 */
	private void recordUserTaskHis(String phoneId, TaskListVo taskListVo, int statusFlag) throws Exception {
		//5. 记录用户抢购历史
		UserTasklistHisVo userTasklistHisVo = new UserTasklistHisVo();
		userTasklistHisVo.setId(UUID.getUUID());
		userTasklistHisVo.setPhoneId(phoneId);
		userTasklistHisVo.setTaskId(taskListVo.getTaskId());
		userTasklistHisVo.setLogoUrl(taskListVo.getLogoUrl());
		userTasklistHisVo.setTaskName(taskListVo.getTaskName());
		userTasklistHisVo.setIncome(taskListVo.getIncome());
		userTasklistHisVo.setStatus(getStatus(statusFlag));
		userTasklistHisVo.setStatusFlag(statusFlag);
		userTasklistHisVo.setCreateDate(DateUtil.getCurrentLongDateTime());
		boolean flag3 = userTasklistHisDao.insertUserTasklistHis(userTasklistHisVo);
		if(flag3) {
			logger.info("记录用户抢购历史成功。");
		}else {
			logger.error("记录用户抢购历史失败。");
		}
	}
	
	/**
	 * 解析操作类型
	 * @param statusFlag
	 * 1=进行中;2=审核中;3=审核成功;4=审核失败;5=已放弃;
	 */
	private static String getStatus(int statusFlag) {
		switch (statusFlag) {
			case 1:
				return "进行中";
			case 2:
				return "审核中";
			case 3:
				return "审核成功";
			case 4:
				return "审核失败";
			case 5:
				return "已放弃";
			default:
				throw new RuntimeException("未知操作类型 statusFlag值");
		}
	}
}