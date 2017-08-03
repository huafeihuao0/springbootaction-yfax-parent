package com.yfax.webapi.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yfax.webapi.dao.AppBannerDao;
import com.yfax.webapi.vo.AppBannerVo;

/**
 * APP广告Banner配置
 * @author Minbo
 */
@Service
public class AppBannerService{
	
	@Autowired
	private AppBannerDao dao;
	
	public List<AppBannerVo> selectAppBannerList(){
		return this.dao.selectAppBannerList();
	}

}
