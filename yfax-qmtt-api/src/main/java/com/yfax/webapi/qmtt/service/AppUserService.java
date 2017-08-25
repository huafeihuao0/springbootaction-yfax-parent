package com.yfax.webapi.qmtt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	public Long selectByRankSum() {
		return this.appUserDao.selectByRankSum();
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
			//添加用户
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
				 Map<String, Object> map = new HashMap<>();
				 map.put("sourceIp", sourceIp);
				 //判断是否是由其他人邀请加入的
				 IpShareCodeVo ipShareCodeVo = this.ipShareCodeDao.selectIpShareCodeIsFromIp(map);
				 logger.info("ipShareCodeVo=" + ipShareCodeVo.toString());
				 AppShareCodeVo appShareCodeVo = this.appShareCodeDao.selectAppShareCodeByShareCode(ipShareCodeVo.getShareCode());
				 logger.info("appShareCodeVo=" + appShareCodeVo.toString());
				 //邀请人信息
				 AppUserVo appUserVo2 = this.appUserDao.selectByPhoneNum(appShareCodeVo.getPhoneNum());
				 logger.info("appUserVo2=" + appUserVo2.toString());
				 if(appUserVo2 != null) {
					 //配置信息
					 AppConfigVo appConfigVo = this.appConfigService.selectAppConfig();
					 JsonResult result = new JsonResult();
					 if(appUserVo2.getFirstInvite() == 0) {
						 //给邀请人首次邀请奖励
						 result = this.awardHisService.addAwardHis(appUserVo2.getPhoneNum(), appConfigVo.getFirstInviteGold()
								 , GlobalUtils.AWARD_TYPE_FIRSTINVITE, null, null, 1, null, null);
						 logger.info("首次邀请奖励，gold=" + appConfigVo.getFirstInviteGold()
								 + "，phoneNum=" + appUserVo2.getPhoneNum() + ", result=" + result.toJsonString());
					 }else {
						//随机金币奖励
						int gold = GlobalUtils.getRanomGold(appConfigVo.getGoldRange());
						result = this.awardHisService.addAwardHis(appUserVo2.getPhoneNum(), gold, 
								GlobalUtils.AWARD_TYPE_INVITE, null, null, null, null, null);
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
						 
						 boolean flag2 = this.shareUserHisDao.insertShareUserHis(shareUserHisVo);
						 boolean flag3 = this.appUserDao.updateUser(appUserVo2);
						 logger.info("flag2=" + flag2 + ", flag3=" + flag3);
						 if(!flag2 && !flag3) {
							 logger.error("邀请人信息更新失败，请检查", new RuntimeException("flag2=" + flag2 + ", flag3=" + flag3));
						 }
					 }else {
						 logger.error("邀请奖励失败，请检查", new RuntimeException("result=" + result.toJsonString()));
					 }
				 }
				 return new JsonResult(ResultCode.SUCCESS);
			}else {
				 return new JsonResult(ResultCode.SUCCESS_FAIL);
			}
		} catch (Exception e) {
			logger.error("用户注册服务异常：" + e.getMessage(), e);
			return new JsonResult(ResultCode.SUCCESS_FAIL);
		}
	}
}
