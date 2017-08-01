package com.yfax.webapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yfax.webapi.dao.SdkChannelConfigDao;
import com.yfax.webapi.vo.SdkChannelConfigVo;

/**
 * SDK平台渠道分成配置
 * @author Minbo
 */
@Service
public class SdkChannelConfigService{
	
	@Autowired
	private SdkChannelConfigDao dao;
	
	public List<SdkChannelConfigVo> selectSdkChannelConfigList(){
		return this.dao.selectSdkChannelConfigList();
	}

}
