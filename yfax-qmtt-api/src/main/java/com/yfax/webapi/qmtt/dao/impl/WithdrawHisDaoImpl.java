package com.yfax.webapi.qmtt.dao.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.webapi.qmtt.vo.WithdrawHisVo;
import com.yfax.webapi.qmtt.dao.WithdrawHisDao;

@Component
public class WithdrawHisDaoImpl implements WithdrawHisDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;;
	
	@Override
	public List<WithdrawHisVo> selectWithdrawHis(String phoneId) {
		return this.sqlSessionTemplate.selectList("selectWithdrawHis", phoneId);
	}

	@Override
	@Transactional
	public boolean insertWithdrawHis(WithdrawHisVo withdrawHisVo) throws Exception {
		int i = this.sqlSessionTemplate.insert("insertWithdrawHis", withdrawHisVo);
		return i > 0 ? true : false;
	}

}
