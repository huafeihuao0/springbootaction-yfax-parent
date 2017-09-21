package com.yfax.webapi.ytt.dao;

import com.yfax.webapi.ytt.vo.UserFeedbackVo;

public interface UserFeedbackDao {
	public boolean insertUserFeedback(UserFeedbackVo userFeedback) throws Exception;
}
