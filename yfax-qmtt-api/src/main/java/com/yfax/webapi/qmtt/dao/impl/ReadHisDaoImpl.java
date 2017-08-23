package com.yfax.webapi.qmtt.dao.impl;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.webapi.qmtt.dao.ReadHisDao;
import com.yfax.webapi.qmtt.vo.ReadHisVo;


@Component
public class ReadHisDaoImpl implements ReadHisDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;;
	
	@Override
	@Transactional
	public boolean insertReadHis(ReadHisVo awardHisVo) throws Exception {
		int i = this.sqlSessionTemplate.insert("insertReadHis", awardHisVo);
		return i > 0 ? true : false;
	}

	@Override
	public List<ReadHisVo> selectReadHisByPhoneNum(String phoneNum) {
		return this.sqlSessionTemplate.selectList("selectReadHisByPhoneNum", phoneNum);
	}

	@Override
	public Long selectReadHisCountByPhoneNum(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne("selectReadHisCountByPhoneNum", map);
	}

	@Override
	public Long selectReadHisCountByPhoneNumAndPrimaryKey(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne("selectReadHisCountByPhoneNumAndPrimaryKey", map);
	}

}
