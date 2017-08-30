package com.yfax.webapi.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.webapi.cfdb.vo.TaskListVo;
import com.yfax.webapi.dao.TaskListDao;

@Component
public class TaskLisDaoImpl implements TaskListDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public TaskListVo selectTaskListById(String taskId) {
		return this.sqlSessionTemplate.selectOne("selectTaskListById", taskId);
	}

	@Override
	@Transactional
	public boolean updateTaskListById(TaskListVo taskListVo) {
		int i = this.sqlSessionTemplate.update("updateTaskListById", taskListVo);
		return i > 0 ? true : false;
	}
}
