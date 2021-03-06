package com.yfax.webapi.ytt.dao.impl;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.webapi.ytt.dao.AwardHisDao;
import com.yfax.webapi.ytt.vo.AwardHisVo;

@Component
public class AwardHisDaoImpl implements AwardHisDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;;
	
	@Override
	@Transactional
	public boolean insertAwardHis(AwardHisVo awardHisVo) throws Exception {
		int i = this.sqlSessionTemplate.insert("insertAwardHis", awardHisVo);
		return i > 0 ? true : false;
	}

	@Override
	public List<AwardHisVo> selectAwardHisByPhoneNum(String phoneNum) {
		return this.sqlSessionTemplate.selectList("selectAwardHisByPhoneNum", phoneNum);
	}

	@Override
	public AwardHisVo selectAwardHisIsCheckIn(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne("selectAwardHisIsFirstLogin", map);
	}

	@Override
	public Long selectUserTotalOfGold(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne("selectUserTotalOfGold", map);
	}

	@Override
	public Long selectUserAwardCount(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne("selectUserAwardCount", map);
	}

	@Override
	public List<AwardHisVo> selectAwardHisCheckList(String phoneNum) {
		return this.sqlSessionTemplate.selectList("selectAwardHisCheckList", phoneNum);
	}

}
