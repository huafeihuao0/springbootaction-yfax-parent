package com.yfax.webapi.qmtt.dao;

import com.yfax.webapi.qmtt.vo.UserFeedbackVo;

public interface UserFeedbackDao {
	public boolean insertUserFeedback(UserFeedbackVo userFeedback) throws Exception;
}
