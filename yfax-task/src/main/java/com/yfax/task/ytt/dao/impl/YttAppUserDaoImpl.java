package com.yfax.task.ytt.dao.impl;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.task.ytt.dao.YttAppUserDao;
import com.yfax.task.ytt.vo.YttAppUserVo;

@Component
public class YttAppUserDaoImpl implements YttAppUserDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;;
	
	@Override
	public YttAppUserVo selectByPhoneNumAndPwd(Map<String, Object> params) {
		return this.sqlSessionTemplate.selectOne("selectYttByPhoneNumAndPwd", params);
	}

	@Override
	public YttAppUserVo selectByPhoneNum(String phoneNum) {
		return this.sqlSessionTemplate.selectOne("selectYttByPhoneNum", phoneNum);
	}

	@Override
	@Transactional
	public boolean deleteTokenByPhoneNum(String phoneNum) {
		int i = this.sqlSessionTemplate.delete("deleteYttByPhoneNum", phoneNum);
		return i > 0 ? true : false;
	}

	@Override
	@Transactional
	public boolean insertUser(YttAppUserVo appUserVo) {
		int i = this.sqlSessionTemplate.delete("insertYttUser", appUserVo);
		return i > 0 ? true : false;
	}

	@Override
	@Transactional
	public boolean updateUser(YttAppUserVo appUserVo) {
		int i = this.sqlSessionTemplate.delete("updateYttUser", appUserVo);
		return i > 0 ? true : false;
	}

	@Override
	public List<YttAppUserVo> selectByRank() {
		return this.sqlSessionTemplate.selectList("selectYttByRank");
	}
	
	@Override
	public Long selectByRankSum() {
		return this.sqlSessionTemplate.selectOne("selectYttByRankSum");
	}

	@Override
	public List<YttAppUserVo> selectByPhoneNumGoldLimit() {
		return this.sqlSessionTemplate.selectList("selectYttByPhoneNumGoldLimit");
	}

	@Override
	public List<YttAppUserVo> selectAllUser() {
		return this.sqlSessionTemplate.selectList("selectYttAllUser");
	}
}
