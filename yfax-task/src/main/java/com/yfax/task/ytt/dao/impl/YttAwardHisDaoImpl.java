package com.yfax.task.ytt.dao.impl;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.task.ytt.dao.YttAwardHisDao;
import com.yfax.task.ytt.vo.YttAwardHisVo;


@Component
public class YttAwardHisDaoImpl implements YttAwardHisDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;;
	
	@Override
	@Transactional
	public boolean insertAwardHis(YttAwardHisVo awardHisVo) throws Exception {
		int i = this.sqlSessionTemplate.insert("insertYttAwardHis", awardHisVo);
		return i > 0 ? true : false;
	}

	@Override
	public List<YttAwardHisVo> selectAwardHisByPhoneNum(String phoneNum) {
		return this.sqlSessionTemplate.selectList("selectYttAwardHisByPhoneNum", phoneNum);
	}

	@Override
	public YttAwardHisVo selectAwardHisIsCheckIn(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne("selectYttAwardHisIsFirstLogin", map);
	}

	@Override
	public Long selectUserTotalOfGold(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne("selectYttUserTotalOfGold", map);
	}

}
