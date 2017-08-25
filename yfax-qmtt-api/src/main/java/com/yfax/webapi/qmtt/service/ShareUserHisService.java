package com.yfax.webapi.qmtt.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yfax.webapi.qmtt.dao.ShareUserHisDao;
import com.yfax.webapi.qmtt.vo.ShareUserHisVo;

/**
 * 邀请徒弟明细记录
 * @author Minbo
 */
@Service
public class ShareUserHisService{
	
	protected static Logger logger = LoggerFactory.getLogger(ShareUserHisService.class);
	
	@Autowired
	private ShareUserHisDao dao;
	
	/**
	 * 根据徒弟手机，找到师傅信息
	 * @param phoneNum
	 * @return
	 */
	public ShareUserHisVo selectShareUserByStudentPhoneNum(String phoneNum) {
		return this.dao.selectShareUserByStudentPhoneNum(phoneNum);
	}

}
