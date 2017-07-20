package com.yfax.webapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yfax.webapi.dao.ApkUrlDao;
import com.yfax.webapi.vo.ApkUrlVo;

/**
 * 分享APK下载链接
 * @author Minbo
 */
@Service
public class ApkUrlService{
	
	@Autowired
	private ApkUrlDao dao;
	
	public ApkUrlVo selectApkUrl(){
		return this.dao.selectApkUrl();
	}

}
