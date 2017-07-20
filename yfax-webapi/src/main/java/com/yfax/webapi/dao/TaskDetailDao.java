package com.yfax.webapi.dao;

import com.yfax.webapi.vo.TaskDetailVo;

public interface TaskDetailDao {
	public TaskDetailVo selectTaskDetailByTaskId(String taskId);
}
