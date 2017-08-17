package com.yfax.webapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yfax.webapi.cfdb.vo.SdkTasklistVo;
import com.yfax.webapi.dao.SdkTasklistDao;
import com.yfax.utils.JsonResult;
import com.yfax.utils.ResultCode;

/**
 * 平台SDK广告记录
 * @author Minbo
 */
@Service
public class SdkTasklistService{
	
	protected static Logger logger = LoggerFactory.getLogger(SdkTasklistService.class);
	
	@Autowired
	private SdkTasklistDao sdkTasklistDao;
	
	
	/**
	 * 新增平台SDK广告记录
	 * @return
	 */
	public JsonResult addSdkTasklist(SdkTasklistVo sdkTasklistVo) {
		try {
			SdkTasklistVo isExist = this.sdkTasklistDao.selectSdkTasklistByAdid(sdkTasklistVo.getAdid());
			if(isExist != null) {
				return new JsonResult(ResultCode.SUCCESS_DUPLICATE);
			}
			boolean flag = this.sdkTasklistDao.insertSdkTasklist(sdkTasklistVo);
			if (flag) {
				return new JsonResult(ResultCode.SUCCESS);
			}else {
				return new JsonResult(ResultCode.SUCCESS_FAIL);
			}
		} catch (Exception e) {
			logger.error("新增平台SDK广告记录：" + e.getMessage(), e);
			return new JsonResult(ResultCode.EXCEPTION);
		}
		
	}
	
}
