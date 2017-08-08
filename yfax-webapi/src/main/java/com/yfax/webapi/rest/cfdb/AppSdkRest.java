package com.yfax.webapi.rest.cfdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yfax.webapi.service.AdvHisService;
import com.yfax.webapi.service.SdkTasklistService;
import com.yfax.webapi.service.UsersService;
import com.yfax.webapi.utils.DateUtil;
import com.yfax.webapi.utils.JsonResult;
import com.yfax.webapi.utils.MD5Util;
import com.yfax.webapi.utils.ResultCode;
import com.yfax.webapi.vo.AdvHisVo;
import com.yfax.webapi.vo.SdkTasklistVo;
import com.yfax.webapi.vo.UsersVo;

/**
 * @author Minbo.He
 * 平台广告接口
 */
@RestController
@RequestMapping("/api/cfdb")
public class AppSdkRest {
	
	protected static Logger logger = LoggerFactory.getLogger(AppSdkRest.class);

	@Autowired
	private UsersService usersService;
	@Autowired
	private AdvHisService advHisService;
	@Autowired
	private SdkTasklistService sdkTasklistService;
	
	/**
	 * 点入-广告平台接口回调数据秘钥
	 */
	private final static String AD_SECRET_DIANRU="*#bUOyFBOI#(@BFW";
	
	/**
	 * 点入-广告平台状态回调接口
	 */
	@RequestMapping("/sendAdvInfo")
	public String sendAdvInfo(String hashid,String appid,String adid,
			String adname,String userid,String mac,String deviceid,
			String source,String point,String time,String active_num,
			String checksum) {
		//1. 校验MD5数据内容
		String parameter= "?hashid=" + hashid + "&appid=" + appid + "&adid=" + adid + "&adname=" + adname + ""
				+ "&userid=" + userid + "&mac=" + mac + "&deviceid=" + deviceid + ""
				+ "&source=" + source + "&point=" + point + "&time=" + time 
				+ "&active_num=" + active_num + "&appsecret=" + AD_SECRET_DIANRU + "";
		logger.info("parameter=[" + parameter+"]");
		String md5Result = MD5Util.encodeByMD5(parameter)	.toLowerCase();	//小写比较	
		if(hashid == null || appid == null || checksum == null) {
			logger.error("参数错误");
			return "{\"message\":\"参数错误\",\"success\":\"false\"}";
		}
		//md5校对结果
		boolean flag = md5Result.equals(checksum.toLowerCase());
		//2. 保存广告平台回调记录
		AdvHisVo advHis = new AdvHisVo();
		advHis.setHashid(hashid);
		advHis.setAppid(appid);;
		advHis.setAdid(adid);
		advHis.setAdname(adname);
		advHis.setUserid(userid);
		advHis.setMac(mac);
		advHis.setDeviceid(deviceid);
		advHis.setSource(source);
		advHis.setPoint(point);
		advHis.setTime(time);
		advHis.setTimeStr(DateUtil.stampToDate(Long.valueOf(time)));
		advHis.setChecksum(checksum.toLowerCase());
		advHis.setCreateDate(DateUtil.getCurrentLongDateTime());
		advHis.setActiveNum(active_num);
		advHis.setResult(flag?"校验正确":"校验失败");
		advHis.setResultSum(md5Result);
		boolean flag2 = this.advHisService.addAdvHis(advHis);
		if(!flag2) {
			logger.warn("回调记录保存失败");
		}
		if(flag) {	
			logger.info("数据校验正确");
			return "{\"message\":\"成功\",\"success\":\"true\"}";
		}else {
			logger.warn("数据校验失败。md5Result=" + md5Result 
					+ ", checksum=" + checksum.toLowerCase());
			return "{\"message\":\"数据校验失败\",\"success\":\"false\"}";
		}
	}
	
	/**
	 * 点入-新增平台SDK广告记录
	 */
	@RequestMapping(value = "/doSdkTasklist", method = {RequestMethod.POST})
	public JsonResult doSdkTasklist(String phoneId, String adid,String cid,
			String intro,String url,String icon,String psize,String title,
			String text1,String text2,String android_url,String active_time,
			String runtime,String curr_note,String active_num,String score) {
		UsersVo users = this.usersService.selectUsersByPhoneId(phoneId);
		if(users != null) {
			SdkTasklistVo sdkTasklistVo = new SdkTasklistVo();
			sdkTasklistVo.setAdid(adid);
			sdkTasklistVo.setCid(cid);
			sdkTasklistVo.setIntro(intro);
			sdkTasklistVo.setUrl(url);
			sdkTasklistVo.setIcon(icon);
			sdkTasklistVo.setPsize(psize);
			sdkTasklistVo.setTitle(title);
			sdkTasklistVo.setText1(text1);
			sdkTasklistVo.setText2(text2);
			sdkTasklistVo.setAndroid_url(android_url);
			sdkTasklistVo.setActive_time(active_time);
			sdkTasklistVo.setRuntime(runtime);
			sdkTasklistVo.setCurr_note(curr_note);
			sdkTasklistVo.setActive_num(active_num);
			sdkTasklistVo.setScore(score);
			String cTime = DateUtil.getCurrentLongDateTime();
			sdkTasklistVo.setCreateDate(cTime);
			sdkTasklistVo.setUpdateDate(cTime);
			return this.sdkTasklistService.addSdkTasklist(sdkTasklistVo);
		}else {
			return new JsonResult(ResultCode.SUCCESS_NO_USER);
		}
	}
	
	/**
	 * 有米-广告平台接口回调数据秘钥
	 */
	private final static String AD_SECRET_YOUMI="";
	
	/**
	 * 有米-广告平台状态回调接口
	 */
	@RequestMapping("/sendAdvInfoYm")
	public String sendAdvInfoYm(String order, String app, String ad, String user, String chn, String points, String sig,
			String adid, String pkg, String device, String time, String price, String trade_type, String _fb) {
		logger.info("||" + order + "||" + app + "||" + user + "||" + chn + "||" + ad + "||" + points);
//		String sigMd5 = md5(dev_server_secret + "||" + order + "||" + app + "||" + user + "||" + chn + "||" + ad + "||" + points).substring(12, 20);
		return "{\"message\":\"成功\",\"success\":\"true\"}";
	}
}
