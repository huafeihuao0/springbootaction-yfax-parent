package com.yfax.webapi.wdkz.dao;

import com.yfax.webapi.wdkz.vo.WdkzAppConfigVo;

public interface WdkzAppConfigDao {
	
	public WdkzAppConfigVo selectAppConfigByVersion(String version);
	
}
