package com.yfax.webapi.wdkz.rest;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yfax.utils.JsonResult;
import com.yfax.utils.ResultCode;
import com.yfax.webapi.wdkz.service.WdkzAppConfigService;
import com.yfax.webapi.wdkz.vo.WdkzAppConfigVo;

/**
 * @author Minbo.He 
 * 微多开助手，查询接口
 */
@RestController
@RequestMapping("/api/wdkz")
public class WdkzAppQueryRest {

	protected static Logger logger = LoggerFactory.getLogger(WdkzAppQueryRest.class);

	@Autowired
	private WdkzAppConfigService wdkzAppConfigService;
	
	/**
	 * 获取app配置数据（修改数据需隔天生效）
	 */
//	@SuppressWarnings("unchecked")
	@RequestMapping("/queryAppConfig")
	public JsonResult queryAppConfig(String version) {
//		String cTime = DateUtil.getCurrentDate();
//		String cacheKey = cTime + "#" + version;
		Map<String, Object> allMap = new HashMap<String, Object>();
//		if(GlobalUtils.flagCache.get(cacheKey)==null) {
			WdkzAppConfigVo wdkzAppConfigVo = this.wdkzAppConfigService.selectAppConfigByVersion(version);
			allMap.put("wdkzAppConfigVo", wdkzAppConfigVo);
//			GlobalUtils.flagCache.put(cacheKey, cacheKey);
//			GlobalUtils.dataCache.put(version, allMap);
//		}else {
//			allMap = (Map<String, Object>) GlobalUtils.dataCache.get(version);
//		}
		return new JsonResult(ResultCode.SUCCESS, allMap);
	}
}