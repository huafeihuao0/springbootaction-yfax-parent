package com.yfax.webapi.dao.impl;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.webapi.cfdb.vo.UserTaskListVo;
import com.yfax.webapi.dao.UserTaskListDao;

@Component
public class UserTaskListDaoImpl implements UserTaskListDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;;

	@Override
	public List<UserTaskListVo> selectUserTaskListByPhoneId(String phoneId) {
		return this.sqlSessionTemplate.selectList("selectUserTaskListByPhoneId", phoneId);
	}

	@Override
	@Transactional
	public boolean deleteUserTask(Map<Object, Object> map) throws Exception{
		int i = this.sqlSessionTemplate.delete("deleteUserTask", map);
		return i > 0 ? true : false;
	}

	@Override
	@Transactional
	public boolean updateIsChecked(Map<Object, Object> map) throws Exception{
		int i = this.sqlSessionTemplate.update("updateIsChecked", map);
		return i > 0 ? true : false;
	}

	@Override
	@Transactional
	public boolean insertUserTask(UserTaskListVo userTaskListVo) throws Exception {
		int i = this.sqlSessionTemplate.insert("insertUserTask", userTaskListVo);
		return i > 0 ? true : false;
	}

	@Override
	@Transactional
	public boolean updateUserTaskById(UserTaskListVo userTaskListVo) throws Exception {
		int i = this.sqlSessionTemplate.update("updateUserTaskById", userTaskListVo);
		return i > 0 ? true : false;
	}

	@Override
	public UserTaskListVo selectUserTaskListById(String id) {
		return this.sqlSessionTemplate.selectOne("selectUserTaskListById", id);
	}

	@Override
	public boolean selectCountIsExist(Map<Object, Object> map) {
		long count = this.sqlSessionTemplate.selectOne("selectCountIsExist", map);
		return count > 0 ? true : false;
	}
}
