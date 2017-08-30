package com.yfax.webapi.dao;

import java.util.List;

import com.yfax.webapi.cfdb.vo.SdkChannelConfigVo;

public interface SdkChannelConfigDao {
	public List<SdkChannelConfigVo> selectSdkChannelConfigList();
	public SdkChannelConfigVo selectSdkChannelConfigByFlag(String channelFlag);
}
