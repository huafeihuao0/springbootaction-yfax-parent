package com.yfax.webapi.qmtt.dao;

import java.util.Map;

import com.yfax.webapi.qmtt.vo.IpShareCodeVo;

public interface IpShareCodeDao {
	public boolean insertIpShareCode(IpShareCodeVo ipShareCodeVo) throws Exception;
	public boolean updateIpShareCode(IpShareCodeVo ipShareCodeVo) throws Exception;
	public IpShareCodeVo selectIpShareCodeByIp(Map<String, Object> map);
	/**
	 * 取邀请人最晚时间的邀请码，成为该人的徒弟
	 * @param map
	 * @return
	 */
	public IpShareCodeVo selectIpShareCodeIsFromIp(Map<String, Object> map);
}
