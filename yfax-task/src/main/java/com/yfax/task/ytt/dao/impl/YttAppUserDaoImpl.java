package com.yfax.task.ytt.dao.impl;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.task.qmtt.dao.AppUserDao;
import com.yfax.task.qmtt.vo.AppUserVo;

@Component
public class YttAppUserDaoImpl implements AppUserDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;;
	
	@Override
	public AppUserVo selectByPhoneNumAndPwd(Map<String, Object> params) {
		return this.sqlSessionTemplate.selectOne("selectByPhoneNumAndPwd", params);
	}

	@Override
	public AppUserVo selectByPhoneNum(String phoneNum) {
		return this.sqlSessionTemplate.selectOne("selectByPhoneNum", phoneNum);
	}

	@Override
	@Transactional
	public boolean deleteTokenByPhoneNum(String phoneNum) {
		int i = this.sqlSessionTemplate.delete("deleteByPhoneNum", phoneNum);
		return i > 0 ? true : false;
	}

	@Override
	@Transactional
	public boolean insertUser(AppUserVo appUserVo) {
		int i = this.sqlSessionTemplate.delete("insertUser", appUserVo);
		return i > 0 ? true : false;
	}

	@Override
	@Transactional
	public boolean updateUser(AppUserVo appUserVo) {
		int i = this.sqlSessionTemplate.delete("updateUser", appUserVo);
		return i > 0 ? true : false;
	}

	@Override
	public List<AppUserVo> selectByRank() {
		return this.sqlSessionTemplate.selectList("selectByRank");
	}
	
	@Override
	public Long selectByRankSum() {
		return this.sqlSessionTemplate.selectOne("selectByRankSum");
	}

	@Override
	public List<AppUserVo> selectByPhoneNumGoldLimit() {
		return this.sqlSessionTemplate.selectList("selectByPhoneNumGoldLimit");
	}

	@Override
	public List<AppUserVo> selectAllUser() {
		return this.sqlSessionTemplate.selectList("selectAllUser");
	}
}
