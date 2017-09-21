package com.yfax.webapi.ytt.dao;

import java.util.List;
import java.util.Map;

import com.yfax.webapi.ytt.vo.AppUserVo;

public interface AppUserDao {
	public AppUserVo selectByPhoneNumAndPwd(Map<String, Object> params);
	public AppUserVo selectByPhoneNum(String phoneNum);
	public boolean deleteTokenByPhoneNum(String phoneNum);
	public boolean insertUser(AppUserVo appUserVo);
	public boolean updateUser(AppUserVo appUserVo);
	public List<AppUserVo> selectByRank();
	public Long selectByRankSum();
	public Long selectByTodaySum(Map<String, Object> params);
	public List<AppUserVo> selectByRankGold();
	public Long selectByTotalGold(Map<String, Object> params);
}
