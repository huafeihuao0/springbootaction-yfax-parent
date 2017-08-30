package com.yfax.webapi.dao;

import com.yfax.webapi.cfdb.vo.SdkTasklistVo;

public interface SdkTasklistDao {
	public boolean insertSdkTasklist(SdkTasklistVo sdkTasklist) throws Exception;
	public SdkTasklistVo selectSdkTasklistByAdid(String adid);
}
