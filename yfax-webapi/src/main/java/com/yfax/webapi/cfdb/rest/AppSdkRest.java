package com.yfax.webapi.cfdb.rest;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yfax.webapi.GlobalUtils;
import com.yfax.webapi.cfdb.vo.AdvHisVo;
import com.yfax.webapi.cfdb.vo.SdkChannelConfigVo;
import com.yfax.webapi.cfdb.vo.SdkTasklistVo;
import com.yfax.webapi.cfdb.vo.UsersVo;
import com.yfax.webapi.service.AdvHisService;
import com.yfax.webapi.service.SdkChannelConfigService;
import com.yfax.webapi.service.SdkTasklistService;
import com.yfax.webapi.service.UsersService;
import com.yfax.utils.DateUtil;
import com.yfax.utils.JsonResult;
import com.yfax.utils.MD5Util;
import com.yfax.utils.NetworkUtil;
import com.yfax.utils.ResultCode;
import com.yfax.utils.StrUtil;

/**
 * @author Minbo.He
 * 冲返单包，平台广告接口
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
	@Autowired
	private SdkChannelConfigService sdkChannelConfigService;
	
	/**
	 * 点入-广告平台接口回调数据秘钥
	 */
	private static String AD_SECRET_DIANRU="*#bUOyFBOI#(@BFW";
	
	/**
	 * 点入-广告平台状态回调接口
	 */
	@RequestMapping("/sendAdvInfo")
	public String sendAdvInfo(String hashid,String appid,String adid,
			String adname,String userid,String mac,String deviceid,
			String source,String point,String time,String active_num,
			String checksum, HttpServletRequest request) {
		//取对应渠道的后台配置秘钥，为空则使用默认值
		SdkChannelConfigVo sdkChannelConfigVo = this.sdkChannelConfigService.selectSdkChannelConfigByFlag(GlobalUtils.DIANRU_CN);
		if(sdkChannelConfigVo != null) {
			AD_SECRET_DIANRU = sdkChannelConfigVo.getSdkPwd();
		}
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
		boolean md5flag = md5Result.equals(checksum.toLowerCase());
		logger.info("md5校对结果" + (md5flag?"校验正确":"校验失败"));
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
		advHis.setResult(md5flag?"校验正确":"校验失败");
		advHis.setResultSum(md5Result);
		boolean flag2 = this.advHisService.addAdvHis(advHis, GlobalUtils.DIANRU, md5flag);
		if(!flag2) {
			logger.warn("回调记录保存失败");
		}
		if(md5flag) {	
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
			String runtime,String curr_note,String active_num,String score, 
			String sdkType) {
		if(sdkType == null) {
			return new JsonResult(ResultCode.PARAMS_ERROR);
		}
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
			sdkTasklistVo.setSdkType(sdkType);
			return this.sdkTasklistService.addSdkTasklist(sdkTasklistVo);
		}else {
			return new JsonResult(ResultCode.SUCCESS_NO_USER);
		}
	}
	
	/**
	 * 有米-广告平台接口回调数据秘钥
	 */
	private static String AD_SECRET_YOUMI="7a57ab415882ce70";	//生产
	
	/**
	 * 有米-广告平台状态回调接口
	 */
	@RequestMapping("/sendAdvInfoYm")
	public String sendAdvInfoYm(String order, String app, String ad, String user, String chn, String points, String sig,
			String adid, String pkg, String device, String time, String price, String trade_type, String _fb, 
			HttpServletRequest request) {
		String ip = NetworkUtil.getIpAddress(request);
		if(!validIpListOfYm(ip)) {
			logger.error("非法请求 from ip=" + ip);
			return "{\"message\":\"非法请求\",\"success\":\"false\"}";
		}
		//取对应渠道的后台配置秘钥，为空则使用默认值
		SdkChannelConfigVo sdkChannelConfigVo = this.sdkChannelConfigService.selectSdkChannelConfigByFlag(GlobalUtils.YOUMI_CN);
		if(sdkChannelConfigVo != null) {
			AD_SECRET_YOUMI = sdkChannelConfigVo.getSdkPwd();
		}
		//1. 校验MD5数据内容
		String parameter= AD_SECRET_YOUMI + "||" + order + "||" + app + "||" + user + "||" + chn + "||" + ad + "||" + points;
		logger.info("parameter=[" + parameter+"]");
		//截取长度
		String md5Result = MD5Util.encodeByMD5(parameter)	.substring(12, 20).toLowerCase();
		if(order == null || app == null || sig == null) {
			logger.error("参数错误");
			return "{\"message\":\"参数错误\",\"success\":\"false\"}";
		}
		//md5校对结果
		boolean md5flag = md5Result.equals(sig.toLowerCase());	//小写比较
		logger.info("md5校对结果" + (md5flag?"校验正确":"校验失败"));
		//2. 保存广告平台回调记录
		AdvHisVo advHis = new AdvHisVo();
		advHis.setHashid(order);
		advHis.setAppid(app);;
		advHis.setAdid(adid);
		advHis.setAdname(ad);
		advHis.setUserid(user);
		advHis.setDeviceid(device);
		advHis.setSource(GlobalUtils.YOUMI_CN);	//有米
		advHis.setPoint(points);
		advHis.setPrice(price);
		advHis.setTime(time);
		advHis.setTimeStr(DateUtil.stampToDate(Long.valueOf(time)));
		advHis.setChecksum(sig.toLowerCase());
		advHis.setCreateDate(DateUtil.getCurrentLongDateTime());
		advHis.setResult(md5flag?"校验正确":"校验失败");
		advHis.setResultSum(md5Result);
		boolean flag2 = this.advHisService.addAdvHis(advHis, GlobalUtils.YOUMI, md5flag);
		if(!flag2) {
			logger.warn("回调记录保存失败");
		}
		if(md5flag) {	
			logger.info("数据校验正确");
			return "{\"message\":\"成功\",\"success\":\"true\"}";
		}else {
			logger.warn("数据校验失败。md5Result=" + md5Result 
					+ ", checksum=" + sig.toLowerCase());
			return "{\"message\":\"数据校验失败\",\"success\":\"false\"}";
		}
	}
	
	/**
	 * 有米有效的ip服务器地址
	 * @param ip
	 * @return
	 */
	private boolean validIpListOfYm(String ip) {
		String myIps = "182.92.82.188,47.94.37.146,";
		//有米服务器ip
		String ips =  myIps + "54.64.29.23,122.226.199.26,58.63.244.57,58.63.244.58,54.223.128.154,54.223.174.146,58.63.244.232,58.63.244.233";
		String[] ipsArr = ips.split(",");
		boolean flag = false;
		for (int i = 0; i < ipsArr.length; i++) {
			if(ipsArr[i].equals(StrUtil.null2Str(ip))) {
				flag = true;
				break;
			}
		}
		return flag;
	}
}
