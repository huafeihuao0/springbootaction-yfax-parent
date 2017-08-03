package com.yfax.webapi.dao;

import com.yfax.webapi.vo.UserFeedbackVo;

public interface UserFeedbackDao {
	public boolean insertUserFeedback(UserFeedbackVo userFeedback) throws Exception;
}
