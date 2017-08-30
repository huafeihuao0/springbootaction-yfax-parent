package com.yfax.webapi.dao;

import com.yfax.webapi.cfdb.vo.AppUpgradeVo;

public interface AppUpgradeDao {
	public AppUpgradeVo selectAppUpgrade();
	public AppUpgradeVo selectAppUpgradeByVersion(AppUpgradeVo appUpgradeVo);
	public AppUpgradeVo selectAppUpgradeByVersionAndPlatform(AppUpgradeVo appUpgradeVo);
}
