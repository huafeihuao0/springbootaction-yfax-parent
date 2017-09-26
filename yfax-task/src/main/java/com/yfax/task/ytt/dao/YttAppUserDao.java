package com.yfax.task.ytt.dao;

import java.util.List;
import java.util.Map;

import com.yfax.task.ytt.vo.YttAppUserVo;

public interface YttAppUserDao {
	public YttAppUserVo selectByPhoneNumAndPwd(Map<String, Object> params);
	public YttAppUserVo selectByPhoneNum(String phoneNum);
	public boolean deleteTokenByPhoneNum(String phoneNum);
	public boolean insertUser(YttAppUserVo appUserVo);
	public boolean updateUser(YttAppUserVo appUserVo);
	public List<YttAppUserVo> selectByRank();
	public Long selectByRankSum();
	public List<YttAppUserVo> selectByPhoneNumGoldLimit();
	public List<YttAppUserVo> selectAllUser();
}
