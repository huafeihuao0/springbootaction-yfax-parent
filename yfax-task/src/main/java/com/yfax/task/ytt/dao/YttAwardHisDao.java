package com.yfax.task.ytt.dao;

import java.util.List;
import java.util.Map;

import com.yfax.task.ytt.vo.YttAwardHisVo;

public interface YttAwardHisDao {
	public boolean insertAwardHis(YttAwardHisVo awardHisVo) throws Exception;
	public List<YttAwardHisVo> selectAwardHisByPhoneNum(String phoneNum);
	public YttAwardHisVo selectAwardHisIsCheckIn(Map<String, Object> map);
	public Long selectUserTotalOfGold(Map<String, Object> map);
}
