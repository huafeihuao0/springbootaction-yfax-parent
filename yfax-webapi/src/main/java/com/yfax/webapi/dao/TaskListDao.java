package com.yfax.webapi.dao;

import com.yfax.webapi.vo.TaskListVo;

public interface TaskListDao {
	public TaskListVo selectTaskListById(String taskId);
	public boolean updateTaskListById(TaskListVo taskListVo);
}
