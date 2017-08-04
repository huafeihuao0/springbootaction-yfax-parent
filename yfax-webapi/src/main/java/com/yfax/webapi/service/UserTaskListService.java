package com.yfax.webapi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.webapi.dao.TaskDetailDao;
import com.yfax.webapi.dao.TaskListDao;
import com.yfax.webapi.dao.UserTaskListDao;
import com.yfax.webapi.dao.UserTasklistHisDao;
import com.yfax.webapi.utils.DateUtil;
import com.yfax.webapi.utils.JsonResult;
import com.yfax.webapi.utils.ResultCode;
import com.yfax.webapi.utils.UUID;
import com.yfax.webapi.vo.TaskDetailVo;
import com.yfax.webapi.vo.TaskListVo;
import com.yfax.webapi.vo.UserTaskListVo;
import com.yfax.webapi.vo.UserTasklistHisVo;
import com.yfax.webapi.xinge.XgServiceApi;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
	private TaskDetailDao taskDetailDao;
	
	@Autowired
	private UserTasklistHisDao userTasklistHisDao;
	
	public List<UserTaskListVo> selectUserTaskListByPhoneId(String phoneId) {
		return this.userTaskListDao.selectUserTaskListByPhoneId(phoneId);
	}
	
	/**
	 * 抢购任务
	 * @param phoneId
	 * @param taskId
	 * @return
	 * 
	 * 注：需多个事务聚合成一个事务时，需service和dao方法都要加上@Transactional才生效
	 */
	@Transactional
	public JsonResult doPanicBuying(String phoneId, String taskId) {
		try {
			//1. 先判断用户是否已做过此任务
			Map<Object, Object> map = new HashMap<>();
			map.put("phoneId", phoneId);
			map.put("taskId", taskId);
			boolean flag = this.userTaskListDao.selectCountIsExist(map);
			if (flag) {
				return new JsonResult(ResultCode.SUCCESS_JUST_ONE);
			}
			
			//2. 获得任务当前剩余数量
			TaskListVo taskListVo = this.taskListDao.selectTaskListById(taskId);
			if (taskListVo.getAmount() <= 0) {
				return new JsonResult(ResultCode.SUCCESS_ALL_GONE);
			}
			
			//3. 任务当前剩余数量减1
			taskListVo.setAmount(taskListVo.getAmount()-1);
			taskListVo.setUpdateDate(DateUtil.getCurrentLongDateTime());
			boolean flag1 = this.taskListDao.updateTaskListById(taskListVo);
			if (!flag1) {
				return new JsonResult(ResultCode.SUCCESS_FAIL);
			}
			
			//4. 当前用户新增一条任务记录
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
				return new JsonResult(ResultCode.SUCCESS_FAIL);
			}else {
				//5. 记录用户任务操作历史
				recordUserTaskHis(phoneId, taskListVo, 1);	//进行中
				
				//6. 返回任务详情数据返回APP
				TaskDetailVo taskDetailVo = this.taskDetailDao.selectTaskDetailByTaskId(taskId);
				map.put("id", id);
				map.put("taskDetailVo", taskDetailVo);
				return new JsonResult(ResultCode.SUCCESS, map);
			}
			
		} catch (Exception e) {
			logger.error("抢购任务异常：" + e.getMessage(), e);
			return new JsonResult(ResultCode.EXCEPTION);
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
				return new JsonResult(ResultCode.SUCCESS_NO_DATA);
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
				return new JsonResult(ResultCode.SUCCESS);
			}else {
				return new JsonResult(ResultCode.SUCCESS_FAIL);
			}
		} catch (Exception e) {
			logger.error("放弃进行中的任务异常：" + e.getMessage(), e);
			return new JsonResult(ResultCode.EXCEPTION);
		}
	}
	
	/**
	 * 提交审核
	 * @param phoneId
	 * @param id 用户任务列表主键ID
	 * @return
	 */
	@Transactional
	public JsonResult doProve(String phoneId, String id, String fields) {
//		fields = "[{\"name\":\"贺敏\",\"mobile\":\"18688200527\",\"imagesUrl\":[{\"1\":\"a.url\",\"2\":\"b.url\",\"3\":\"c.url\"}]}]";
		
		UserTaskListVo userTaskListVo = this.userTaskListDao.selectUserTaskListById(id);
		if(userTaskListVo == null) {
			return new JsonResult(ResultCode.SUCCESS_NO_DATA);
		}
		if(userTaskListVo.getStatusFlag() == 1) {	//当前状态需为进行中
			userTaskListVo.setId(id);
			userTaskListVo.setStatus("审核中");
			userTaskListVo.setStatusFlag(2);
			userTaskListVo.setUpdateDate(DateUtil.getCurrentLongDateTime());
			
			//处理fields字段
			TaskDetailVo taskDetailVo = this.taskDetailDao.selectTaskDetailByTaskId(userTaskListVo.getTaskId());
			//String config = "[{\"name\":\"name\",\"label\":\"姓名\",\"type\":\"string\",\"placeholder\":\"请输入姓名\"},{\"name\":\"mobile\",\"type\":\"string\",\"label\":\"手机号\",\"placeholder\":\"请输入手机号\"}]";
			String configFields = taskDetailVo.getFields();
			List<String> list = new ArrayList<String>();
			JSONArray configArray = JSONArray.fromObject(configFields);
			for (int j = 0; j < configArray.size(); j++) {
				JSONObject jsonObj = configArray.getJSONObject(j);
				list.add(jsonObj.getString("name"));
			}
			
			JSONArray fieldsArray = JSONArray.fromObject(fields);
			for (int j = 0; j < fieldsArray.size(); j++) {
				JSONObject jsonObj = fieldsArray.getJSONObject(j);
				
				for (int i = 0; i < list.size(); i++) {
					if(list.get(i).equals("name")) {	//姓名
						userTaskListVo.setProveName(String.valueOf(jsonObj.get(list.get(i))));
					}else if(list.get(i).equals("mobile")) {	//手机号码
						userTaskListVo.setProveMobile(String.valueOf(jsonObj.get(list.get(i))));
					}else {
						if(userTaskListVo.getProveColumn1() == null) {
							userTaskListVo.setProveColumn1(String.valueOf(jsonObj.get(list.get(i))));
						}else {
							userTaskListVo.setProveColumn2(String.valueOf(jsonObj.get(list.get(i))));
						}
						
					}
				}
				
				String proveImagesUrl = "";
				String imagesUrl = jsonObj.get("imagesUrl")==null?"":jsonObj.get("imagesUrl").toString();
				if(!imagesUrl.equals("")) {
					JSONArray imagesUrlArray = JSONArray.fromObject(imagesUrl);
					for (int m = 0; m < imagesUrlArray.size(); m++) {
						JSONObject jsonObjImages = imagesUrlArray.getJSONObject(m);
						for (int n = 0; n < jsonObjImages.size(); n++) {
							proveImagesUrl += jsonObjImages.get(String.valueOf(n+1))+"#";
						}
					}
					userTaskListVo.setProveImagesUrl(proveImagesUrl.substring(0, proveImagesUrl.length()-1));
				}else {
					if(taskDetailVo.getIsUpload()!=null && 
							taskDetailVo.getIsUpload() == 1) {
						logger.error("上传图片参数不能为空");
						return new JsonResult(ResultCode.PARAMS_ERROR);
					}
				}
			}
			
			try {
				boolean flag = this.userTaskListDao.updateUserTaskById(userTaskListVo);
				//3. 记录用户任务操作历史
				TaskListVo taskListVo = this.taskListDao.selectTaskListById(userTaskListVo.getTaskId());
				recordUserTaskHis(phoneId, taskListVo, 2);	//审核中
				if (flag) {
					return new JsonResult(ResultCode.SUCCESS);
				}else {
					return new JsonResult(ResultCode.SUCCESS_FAIL);
				}
			} catch (Exception e) {
				logger.error("提交审核异常：" + e.getMessage(), e);
				return new JsonResult(ResultCode.EXCEPTION);
			}
		}else {
			return new JsonResult(ResultCode.SUCCESS_NOT_RIGHT);
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
		try {
			return this.userTaskListDao.updateIsChecked(map);
		} catch (Exception e) {
			logger.error("设置任务已查看标识异常：" + e.getMessage(), e);
			return false;
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
			logger.debug("记录用户抢购历史成功。");
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
	
	/**
	 * rest推送测试用
	 */
	public String testPushNotify(String phoneId, int type) {
		String result = "";
		if(type == 1) {
			result =  XgServiceApi.pushNotify(phoneId, "通知栏消息", "恭喜获得奖励，任务[XXX]已完成，获得收益：XXX元");
		}else if(type == 2){
			result =  XgServiceApi.pushNotifyByMessage(phoneId, "透传消息", "恭喜获得奖励，任务[XXX]已完成，获得收益：XXX元");
		}
		logger.info("推送通知给用户[phoneId=" + phoneId + ", type=" + type + "]，推送发送结果result=" + result);
		return result;
	}
}