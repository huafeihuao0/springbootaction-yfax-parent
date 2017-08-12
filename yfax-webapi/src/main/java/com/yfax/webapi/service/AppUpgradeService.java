package com.yfax.webapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yfax.webapi.cfdb.vo.AppUpgradeVo;
import com.yfax.webapi.dao.AppUpgradeDao;

/**
 * APP版本检查更新
 * @author Minbo
 */
@Service
public class AppUpgradeService{
	
	@Autowired
	private AppUpgradeDao dao;
	
	public AppUpgradeVo selectAppUpgrade(){
		return this.dao.selectAppUpgrade();
	}

}
