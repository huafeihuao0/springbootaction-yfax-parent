package com.yfax.webapi.dao;

import java.util.List;

import com.yfax.webapi.cfdb.vo.AppBannerVo;

public interface AppBannerDao {
	public List<AppBannerVo> selectAppBannerList();
}
