package com.yfax.webapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yfax.webapi.cfdb.vo.UserFeedbackVo;
import com.yfax.webapi.dao.UserFeedbackDao;

/**
 * 用户反馈
 * @author Minbo
 */
@Service
public class UserFeedbackService{
	
	protected static Logger logger = LoggerFactory.getLogger(UserFeedbackService.class);
	
	@Autowired
	private UserFeedbackDao userFeedbackDao;
	
	public boolean addUserFeedback(UserFeedbackVo userFeedbackVo){
		try {
			return this.userFeedbackDao.insertUserFeedback(userFeedbackVo);
		} catch (Exception e) {
			logger.error("新增用户反馈记录异常：" + e.getMessage(), e);
			return false;
		}
	}

}
