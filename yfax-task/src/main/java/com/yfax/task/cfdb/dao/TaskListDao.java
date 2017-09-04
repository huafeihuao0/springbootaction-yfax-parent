package com.yfax.task.cfdb.dao;

import com.yfax.task.cfdb.vo.TaskListVo;

public interface TaskListDao {
	public TaskListVo selectTaskListById(String taskId);
	public boolean updateTaskListById(TaskListVo taskListVo);
}
