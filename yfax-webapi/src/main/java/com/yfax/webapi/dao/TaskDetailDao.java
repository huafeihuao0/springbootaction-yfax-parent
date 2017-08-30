package com.yfax.webapi.dao;

import com.yfax.webapi.cfdb.vo.TaskDetailVo;

public interface TaskDetailDao {
	public TaskDetailVo selectTaskDetailByTaskId(String taskId);
}
