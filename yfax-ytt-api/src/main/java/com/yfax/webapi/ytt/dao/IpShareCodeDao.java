package com.yfax.webapi.ytt.dao;

import java.util.Map;

import com.yfax.webapi.ytt.vo.IpShareCodeVo;

public interface IpShareCodeDao {
	public boolean insertIpShareCode(IpShareCodeVo ipShareCodeVo) throws Exception;
	public boolean updateIpShareCode(IpShareCodeVo ipShareCodeVo) throws Exception;
	public IpShareCodeVo selectIpShareCodeByIp(Map<String, Object> map);
	/**
	 * 取邀请人最晚时间的邀请码，成为该人的徒弟，只查未使用的isUsed=1标识的数据
	 * @param map
	 * @return
	 */
	public IpShareCodeVo selectIpShareCodeIsFromIp(Map<String, Object> map);
}
