package com.yfax.webapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yfax.webapi.cfdb.vo.SdkChannelConfigVo;
import com.yfax.webapi.dao.SdkChannelConfigDao;

/**
 * SDK平台渠道配置
 * @author Minbo
 */
@Service
public class SdkChannelConfigService{
	
	@Autowired
	private SdkChannelConfigDao dao;
	
	public List<SdkChannelConfigVo> selectSdkChannelConfigList(){
		return this.dao.selectSdkChannelConfigList();
	}
	
	public SdkChannelConfigVo selectSdkChannelConfigByFlag(String channelFlag){
		return this.dao.selectSdkChannelConfigByFlag(channelFlag);
	}

}
