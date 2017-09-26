package com.yfax.task.ytt.dao.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.task.ytt.dao.YttBalanceHisDao;
import com.yfax.task.ytt.vo.YttBalanceHisVo;


@Component
public class YttBalanceHisDaoImpl implements YttBalanceHisDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;;
	
	@Override
	@Transactional
	public boolean insertBalanceHis(YttBalanceHisVo balanceHisVo) throws Exception {
		int i = this.sqlSessionTemplate.insert("insertYttBalanceHis", balanceHisVo);
		return i > 0 ? true : false;
	}

	@Override
	public List<YttBalanceHisVo> selectBalanceHisByPhoneNum(String phoneNum) {
		return this.sqlSessionTemplate.selectList("selectYttBalanceHisByPhoneNum", phoneNum);
	}

}
