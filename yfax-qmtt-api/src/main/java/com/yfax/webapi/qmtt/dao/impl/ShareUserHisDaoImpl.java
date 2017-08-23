package com.yfax.webapi.qmtt.dao.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.webapi.qmtt.dao.ShareUserHisDao;
import com.yfax.webapi.qmtt.vo.ShareUserHisVo;


@Component
public class ShareUserHisDaoImpl implements ShareUserHisDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;;
	
	@Override
	@Transactional
	public boolean insertShareUserHis(ShareUserHisVo userSms) throws Exception {
		int i = this.sqlSessionTemplate.insert("insertShareUserHis", userSms);
		return i > 0 ? true : false;
	}

	@Override
	public Long selectCountByPhoneNum(String phoneNum) {
		return this.sqlSessionTemplate.selectOne("selectCountByPhoneNum", phoneNum);
	}
	
}
