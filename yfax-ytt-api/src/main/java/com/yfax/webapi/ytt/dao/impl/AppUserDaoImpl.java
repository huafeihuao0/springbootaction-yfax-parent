package com.yfax.webapi.ytt.dao.impl;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.webapi.ytt.dao.AppUserDao;
import com.yfax.webapi.ytt.vo.AppUserVo;

@Component
public class AppUserDaoImpl implements AppUserDao {

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
	public Long selectByTodaySum(Map<String, Object> params) {
		return this.sqlSessionTemplate.selectOne("selectByTodaySum", params);
	}

	@Override
	public List<AppUserVo> selectByRankGold() {
		return this.sqlSessionTemplate.selectList("selectByRankGold");
	}

	@Override
	public Long selectByTotalGold(Map<String, Object> params) {
		return this.sqlSessionTemplate.selectOne("selectByTotalGold", params);
	}
}
