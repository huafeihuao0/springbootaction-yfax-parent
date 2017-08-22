package com.yfax.webapi.qmtt.dao;

import com.yfax.webapi.qmtt.vo.IpShareCodeVo;

public interface IpShareCodeDao {
	public boolean insertIpShareCode(IpShareCodeVo ipShareCodeVo) throws Exception;
	public IpShareCodeVo selectIpShareCodeByIp(String sourceIp);
}
