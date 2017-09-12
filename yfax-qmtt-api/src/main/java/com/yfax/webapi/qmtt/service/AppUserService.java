package com.yfax.webapi.qmtt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.common.xinge.XgServiceApi;
import com.yfax.utils.DateUtil;
import com.yfax.utils.JsonResult;
import com.yfax.utils.NetworkUtil;
import com.yfax.utils.ResultCode;
import com.yfax.utils.UUID;
import com.yfax.webapi.GlobalUtils;
import com.yfax.webapi.qmtt.dao.AppShareCodeDao;
import com.yfax.webapi.qmtt.dao.AppUserDao;
import com.yfax.webapi.qmtt.dao.IpShareCodeDao;
import com.yfax.webapi.qmtt.dao.ShareUserHisDao;
import com.yfax.webapi.qmtt.vo.AppConfigVo;
import com.yfax.webapi.qmtt.vo.AppShareCodeVo;
import com.yfax.webapi.qmtt.vo.AppUserVo;
import com.yfax.webapi.qmtt.vo.IpShareCodeVo;
import com.yfax.webapi.qmtt.vo.ShareUserHisVo;

import javassist.bytecode.annotation.StringMemberValue;
import net.sf.json.JSONObject;

/**
 * 用户管理服务
 * @author Minbo
 */
@Service
public class AppUserService {
	
	protected static Logger logger = LoggerFactory.getLogger(AppUserService.class);
	
	@Autowired
	private AppUserDao appUserDao;
	@Autowired
	private IpShareCodeDao ipShareCodeDao;
	@Autowired
	private AppShareCodeDao appShareCodeDao;
	@Autowired
	private ShareUserHisDao shareUserHisDao;
	@Autowired
	private AwardHisService awardHisService;
	@Autowired
	private AppConfigService appConfigService;
	
	public AppUserVo selectByPhoneNum(String phoneNum) {
		return this.appUserDao.selectByPhoneNum(phoneNum);
	}
	
	public AppUserVo selectByPhoneNumAndPwd(Map<String, Object> params) {
		return this.appUserDao.selectByPhoneNumAndPwd(params);
	}
	
	public List<AppUserVo> selectByRank() {
		return this.appUserDao.selectByRank();
	}
	
	public List<AppUserVo> selectByRankGold() {
		return this.appUserDao.selectByRankGold();
	}
	
	public Long selectByRankSum() {
		return this.appUserDao.selectByRankSum();
	}
	
	public Long selectByTodaySum(Map<String, Object> params) {
		return this.appUserDao.selectByTodaySum(params);
	}
	
	public Long selectByTotalGold(Map<String, Object> params) {
		return this.appUserDao.selectByTotalGold(params);
	}
	       
	@Transactional
	public boolean doLoginOut(String phoneNum) {
		return this.appUserDao.deleteTokenByPhoneNum(phoneNum);
	}
	
	@Transactional
	public boolean addUser(AppUserVo appUserVo) {
		return this.appUserDao.insertUser(appUserVo);
	}

	@Transactional
	public boolean modifyUser(AppUserVo appUserVo) {
		return this.appUserDao.updateUser(appUserVo);
	}
	
	@Transactional
	public JsonResult registerUser(String phoneNum, String userPwd, HttpServletRequest request) {
		try {
			String sourceIp = NetworkUtil.getIpAddress(request);
			//1. 添加用户
			AppUserVo appUserVo = new AppUserVo();
			appUserVo.setPhoneNum(phoneNum);;
			appUserVo.setUserPwd(userPwd);
			appUserVo.setGold("0");
			appUserVo.setBalance("0.00");
			String cTime = DateUtil.getCurrentLongDateTime();
			appUserVo.setRegisterDate(cTime);
			appUserVo.setLastLoginDate(cTime);
			appUserVo.setUpdateDate(cTime);
			boolean flag = this.appUserDao.insertUser(appUserVo);
			if(flag) {
				 //2. 注册随机奖励
				 AppConfigVo appConfigVo = this.appConfigService.selectAppConfig();
				 int regGold = GlobalUtils.getRanomGold(appConfigVo.getRegGoldRange());
				 JsonResult regResult = this.awardHisService.addAwardHis(appUserVo.getPhoneNum(), regGold, 
						GlobalUtils.AWARD_TYPE_REGISTER, null, null, null, null, null);
				 logger.info("注册随机奖励，regGold=" + regGold + "，phoneNum=" + appUserVo.getPhoneNum() 
						 + ", result=" + regResult.toJsonString());
				 
				 //3. 判断是否是由其他人邀请加入的
				 this.processInvite(sourceIp, appUserVo, appConfigVo, cTime);
				 return new JsonResult(ResultCode.SUCCESS);
			}else {
				 return new JsonResult(ResultCode.SUCCESS_FAIL);
			}
		} catch (Exception e) {
			logger.error("用户注册服务异常：" + e.getMessage(), e);
			return new JsonResult(ResultCode.SUCCESS_FAIL);
		}
	}
	
	/**
	 * 处理注册用户是否被邀请逻辑
	 * @throws Exception 
	 */
	private void processInvite(String sourceIp, AppUserVo appUserVo, AppConfigVo appConfigVo, String cTime){
		 logger.info("判断是否是由其他人邀请加入的...");
		 Map<String, Object> map = new HashMap<>();
		 map.put("sourceIp", sourceIp);
		 IpShareCodeVo ipShareCodeVo = this.ipShareCodeDao.selectIpShareCodeIsFromIp(map);
		 if(ipShareCodeVo != null) {
			 logger.info("ipShareCodeVo=" + ipShareCodeVo.toString());
			 AppShareCodeVo appShareCodeVo = this.appShareCodeDao.selectAppShareCodeByShareCode(ipShareCodeVo.getShareCode());
			 if(appShareCodeVo != null){
				 logger.info("appShareCodeVo=" + appShareCodeVo.toString());
				 //邀请人信息
				 AppUserVo appUserVo2 = this.appUserDao.selectByPhoneNum(appShareCodeVo.getPhoneNum());
				 if(appUserVo2 != null) {
					 logger.info("appUserVo2=" + appUserVo2.toString());
					 JsonResult result = new JsonResult();
					 int awardGold = 0;
					 if(appUserVo2.getFirstInvite() == 0) {
						 //给邀请人首次邀请奖励
						 result = this.awardHisService.addAwardHis(appUserVo2.getPhoneNum(), appConfigVo.getFirstInviteGold()
								 , GlobalUtils.AWARD_TYPE_FIRSTINVITE, null, null, 1, null, null);
						 awardGold = appConfigVo.getFirstInviteGold();
						 logger.info("首次邀请奖励，gold=" + appConfigVo.getFirstInviteGold()
								 + "，phoneNum=" + appUserVo2.getPhoneNum() + ", result=" + result.toJsonString());
					 }else {
						//随机金币奖励
						int gold = GlobalUtils.getRanomGold(appConfigVo.getGoldRange());
						result = this.awardHisService.addAwardHis(appUserVo2.getPhoneNum(), gold, 
								GlobalUtils.AWARD_TYPE_INVITE, null, null, null, null, null);
						awardGold = gold;
						logger.info("邀请随机奖励，gold=" + gold + "，phoneNum=" + appUserVo2.getPhoneNum() 
							+ ", result=" + result.toJsonString());
					 }
					 //奖励成功
					 if(result.getCode().equals("200")) {
						 //1. 记录徒弟明细数据
						 ShareUserHisVo shareUserHisVo = new ShareUserHisVo();
						 shareUserHisVo.setId(UUID.getUUID());
						 shareUserHisVo.setMasterPhoneNum(appUserVo2.getPhoneNum());
						 shareUserHisVo.setStudentPhoneNum(appUserVo.getPhoneNum());
						 shareUserHisVo.setCreateDate(cTime);
						 shareUserHisVo.setUpdateDate(cTime);
						 //2. 更新邀请人数据
						 appUserVo2.setStudents(appUserVo2.getStudents() + 1);	//被邀请人的徒弟数加1
						 appUserVo2.setUpdateDate(cTime);
						 appUserVo2.setFirstInvite(1);
						 //3. 更新ip与邀请码的isUsed标识位
						 ipShareCodeVo.setIsUsed(2);		//该记录已使用
						 ipShareCodeVo.setUpdateDate(cTime);
						 
						 try {
							 boolean flag2 = this.shareUserHisDao.insertShareUserHis(shareUserHisVo);
							 boolean flag3 = this.appUserDao.updateUser(appUserVo2);
							 boolean flag4 = this.ipShareCodeDao.updateIpShareCode(ipShareCodeVo);
							 logger.info("flag2=" + flag2 + ", flag3=" + flag3
									 + ", flag4=" + flag4);
							 if(!flag2 && !flag3 && !flag4) {
								 logger.error("邀请人信息更新失败，请检查", new RuntimeException("flag2=" + flag2 + ", flag3=" + flag3 
										 + ", flag4=" + flag4));
							 }else {
								 if(awardGold>0) {
									 //推送用户通知
									 String result2 =  XgServiceApi.pushNotifyByMessage(appUserVo2.getPhoneNum(), "恭喜获得奖励", 
											 	"恭喜您获得邀请奖励" + awardGold + "金币", 
												GlobalUtils.XG_ACCESS_ID, GlobalUtils.XG_SECRET_KEY);
									 logger.info("推送通知给用户[phoneNum=" + appUserVo2.getPhoneNum() + "]，推送发送结果result=" + result2);
								 }
							 }
					 	 } catch (Exception e) {
							logger.error("注册时邀请处理异常：" + e.getMessage(), e);
						 }
					 }else {
						 logger.error("邀请奖励失败，请检查", new RuntimeException("result=" + result.toJsonString()));
					 }
				 }
			 }
		 }
	}
	
	/**
	 * rest推送测试用
	 */
	public String testPushNotify(String phoneId, int type) {
		String result = "";
		Map<String, Object> map = new HashMap<>();
		map.put("task", "XXX");
		map.put("income", "100");
		JSONObject json = JSONObject.fromObject(map);
		if(type == 1) {
//			result =  XgServiceApi.pushNotify(phoneId, "通知栏消息", "恭喜获得奖励，任务[XXX]已完成，获得收益：XXX元", GlobalUtils.XG_ACCESS_ID, GlobalUtils.XG_SECRET_KEY);
			result = XgServiceApi.pushNotify(phoneId, "通知栏消息", json.toString(), GlobalUtils.XG_ACCESS_ID, GlobalUtils.XG_SECRET_KEY);
		}else if(type == 2){
//			result =  XgServiceApi.pushNotifyByMessage(phoneId, "透传消息", "恭喜获得奖励，任务[XXX]已完成，获得收益：XXX元", GlobalUtils.XG_ACCESS_ID, GlobalUtils.XG_SECRET_KEY);
			result = XgServiceApi.pushNotifyByMessage(phoneId, "透传消息", json.toString(), GlobalUtils.XG_ACCESS_ID, GlobalUtils.XG_SECRET_KEY);;
		}
		logger.info("推送通知给用户[phoneId=" + phoneId + ", type=" + type + "]，推送发送结果result=" + result);
		return result;
	}
}
