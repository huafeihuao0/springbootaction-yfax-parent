package com.yfax.webapi.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.webapi.dao.TaskListDao;
import com.yfax.webapi.dao.UserTaskListDao;
import com.yfax.webapi.utils.DateUtil;
import com.yfax.webapi.utils.JsonResult;
import com.yfax.webapi.utils.ResultCode;
import com.yfax.webapi.utils.UUID;
import com.yfax.webapi.vo.TaskListVo;
import com.yfax.webapi.vo.UserTaskListVo;

/**
 * 用户任务管理
 * @author Minbo
 * 1. APP每次同时只能做一个任务
 */
@Service
public class UserTaskListService {

	@Autowired
	private UserTaskListDao userTaskListDao;
	
	@Autowired
	private TaskListDao taskListDao;
	
	public List<UserTaskListVo> selectUserTaskListByPhoneId(String phoneId) {
		return this.userTaskListDao.selectUserTaskListByPhoneId(phoneId);
	}
	
	/**
	 * 抢购任务
	 * @param phoneId
	 * @param taskId
	 * @return
	 */
	@Transactional
	public JsonResult doPanicBuying(String phoneId, String taskId) {
		try {
			//TODO 用户可以对同一个任务做多次吗？前一个任务已经审核完成并成功的情况下
			
			
			//1. 获得任务当前剩余数量
			TaskListVo taskListVo = this.taskListDao.selectTaskListById(taskId);
			if (taskListVo.getAmount() <= 0) {
				return new JsonResult(ResultCode.SUCCESS_ALL_GONE, "今天的量已经跑光了", null);
			}
			
			//2. 任务当前剩余数量减1
			taskListVo.setAmount(taskListVo.getAmount()-1);
			taskListVo.setUpdateDate(DateUtil.getCurrentLongDateTime());
			boolean flag1 = this.taskListDao.updateTaskListById(taskListVo);
			if (!flag1) {
				return new JsonResult(ResultCode.SUCCESS_FAIL, "失败，请重试", null);
			}
			
			//3. 当前用户新增一条任务记录
			UserTaskListVo userTaskListVo = new UserTaskListVo();
			String id = UUID.getUUID();
			userTaskListVo.setId(id);
			userTaskListVo.setPhoneId(phoneId);
			userTaskListVo.setTaskId(taskListVo.getTaskId());
			userTaskListVo.setLogoUrl(taskListVo.getLogoUrl());
			userTaskListVo.setTaskName(taskListVo.getTaskName());
			userTaskListVo.setTaskTag(taskListVo.getTaskTag());
			userTaskListVo.setAmount(taskListVo.getAmount());
			userTaskListVo.setIncome(taskListVo.getIncome());
			userTaskListVo.setOrderNo(taskListVo.getOrderNo());
			userTaskListVo.setGoUrl(taskListVo.getGoUrl());
			userTaskListVo.setStatus("进行中");
			userTaskListVo.setStatusFlag(1);
			String cTime = DateUtil.getCurrentLongDateTime();
			userTaskListVo.setCreateDate(cTime);
			userTaskListVo.setUpdateDate(cTime);
			boolean flag2 = this.userTaskListDao.insertUserTask(userTaskListVo);
			if (!flag2) {
				return new JsonResult(ResultCode.SUCCESS_FAIL, "失败，请重试", null);
			}else {
				return new JsonResult(ResultCode.SUCCESS, "成功", null);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonResult(ResultCode.EXCEPTION, "发生异常", null);
		}
	}

	/**
	 * 放弃进行中的任务
	 * @param phoneId
	 * @param taskId
	 * @return
	 */
	@Transactional
	public JsonResult abandonUserTask(String phoneId, String taskId) {
		try {
			Map<Object, Object> map = new HashMap<>();
			map.put("phoneId", phoneId);
			map.put("taskId", taskId);
			//1. 放弃任务
			boolean flag = this.userTaskListDao.deleteUserTask(map);
			if (!flag) {
				return new JsonResult(ResultCode.SUCCESS_NO_DATA, "数据为空", null);
			}
			//2. 获得任务当前剩余数量
			TaskListVo taskListVo = this.taskListDao.selectTaskListById(taskId);
			//3. 任务当前剩余数量减1
			taskListVo.setAmount(taskListVo.getAmount()+1);
			taskListVo.setUpdateDate(DateUtil.getCurrentLongDateTime());
			boolean flag1 = this.taskListDao.updateTaskListById(taskListVo);
			if (flag1) {
				return new JsonResult(ResultCode.SUCCESS, "成功", null);
			}else {
				return new JsonResult(ResultCode.SUCCESS_FAIL, "失败，请重试", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new JsonResult(ResultCode.EXCEPTION, "发生异常", null);
		}
	}
	
	/**
	 * 提交审核
	 * @param phoneId
	 * @param id 用户任务列表主键ID
	 * @return
	 */
	public JsonResult doProve(String phoneId, String id) {
		UserTaskListVo userTaskListVo = this.userTaskListDao.selectUserTaskListById(id);
		if(userTaskListVo == null) {
			return new JsonResult(ResultCode.SUCCESS_NO_DATA, "数据为空", null);
		}
		if(userTaskListVo.getStatusFlag() == 1) {	//当前状态需为进行中
			userTaskListVo.setId(id);
			userTaskListVo.setStatus("审核中");
			userTaskListVo.setStatusFlag(2);
			userTaskListVo.setUpdateDate(DateUtil.getCurrentLongDateTime());
			try {
				boolean flag = this.userTaskListDao.updateUserTaskById(userTaskListVo);
				if (flag) {
					return new JsonResult(ResultCode.SUCCESS, "成功", null);
				}else {
					return new JsonResult(ResultCode.SUCCESS_FAIL, "失败，请重试", null);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return new JsonResult(ResultCode.EXCEPTION, "发生异常", null);
			}
		}else {
			return new JsonResult(ResultCode.SUCCESS_NOT_RIGHT, "不支持此操作", null);
		}
	}
	
	/**
	 * 设置任务已查看标识
	 * @param id
	 * @return
	 */
	public boolean setIsChecked(String id) {
		Map<Object, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("updateDate", DateUtil.getCurrentLongDateTime());
		try {
			return this.userTaskListDao.updateIsChecked(map);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}