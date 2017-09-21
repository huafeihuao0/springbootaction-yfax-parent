package com.yfax.webapi.ytt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.common.xinge.XgServiceApi;
import com.yfax.utils.DateUtil;
import com.yfax.utils.JsonResult;
import com.yfax.utils.ResultCode;
import com.yfax.utils.UUID;
import com.yfax.webapi.GlobalUtils;
import com.yfax.webapi.ytt.dao.AppUserDao;
import com.yfax.webapi.ytt.dao.AwardHisDao;
import com.yfax.webapi.ytt.dao.ReadHisDao;
import com.yfax.webapi.ytt.dao.ShareUserHisDao;
import com.yfax.webapi.ytt.dao.StudentConfigDao;
import com.yfax.webapi.ytt.vo.AppUserVo;
import com.yfax.webapi.ytt.vo.AwardHisVo;
import com.yfax.webapi.ytt.vo.ReadHisVo;
import com.yfax.webapi.ytt.vo.ShareUserHisVo;
import com.yfax.webapi.ytt.vo.StudentConfigVo;

/**
 * 记录用户金币奖励记录
 * @author Minbo
 */
@Service
public class AwardHisService{
	
	protected static Logger logger = LoggerFactory.getLogger(AwardHisService.class);
	
	@Autowired
	private AwardHisDao awardHisDao;
	@Autowired
	private AppUserDao appUserDao;
	@Autowired
	private ReadHisDao readHisDao;
	@Autowired
	private ShareUserHisDao shareUserHisDao;
	@Autowired
	private StudentConfigDao studentConfigDao;
	
	/**
	 * @param phoneNum
	 * @param gold
	 * @param awardType
	 * @param firstRead
	 * @param firstShare
	 * @param firstInvite
	 * @param readHisId
	 * @return
	 */
	@Transactional
	public JsonResult addAwardHis(String phoneNum, int gold, Integer awardType, 
			Integer firstRead, Integer firstShare, Integer firstInvite, String readHisId, Integer dailyCheckIn){
		try {
			//1. 记录奖励明细
			AwardHisVo awardHisVo = new AwardHisVo();
			awardHisVo.setId(UUID.getUUID());
			awardHisVo.setPhoneNum(phoneNum);
			awardHisVo.setAwardType(awardType);
			awardHisVo.setAwardName(GlobalUtils.getAwardTypeName(awardType));
			awardHisVo.setGold("+" + String.valueOf(gold));
			String cTime = DateUtil.getCurrentLongDateTime();
			awardHisVo.setCreateDate(cTime);
			awardHisVo.setUpdateDate(cTime);
			//2. 更新用户金币余额
			AppUserVo appUserVo = this.appUserDao.selectByPhoneNum(phoneNum);
			int old = Integer.valueOf(appUserVo.getGold());
			int sum = Integer.valueOf(appUserVo.getGold()) + gold;
			appUserVo.setGold(String.valueOf(sum));
			appUserVo.setUpdateDate(cTime);
			appUserVo.setFirstRead(firstRead);
			appUserVo.setFirstShare(firstShare);
			appUserVo.setFirstInvite(firstInvite);
			//今日签到标识
			appUserVo.setDailyCheckIn(dailyCheckIn);	
			logger.info("手机号码phoneNum=" + phoneNum + ", 原金币余额gold=" + old + ", 奖励金币gold=" + gold 
				+ ", 更新金币总余额sum=" + sum + ", 奖励类型awardType=" + awardType);
			boolean flag =  this.awardHisDao.insertAwardHis(awardHisVo);
			boolean flag1 = this.appUserDao.updateUser(appUserVo);
			if(flag && flag1) {
				//3. 记录文章已获取金币标识
				if(awardType == GlobalUtils.AWARD_TYPE_FIRSTREAD 
						|| awardType == GlobalUtils.AWARD_TYPE_READ) {
					ReadHisVo readHisVo = new ReadHisVo();
					readHisVo.setId(readHisId);
					readHisVo.setIsAward(1);		//已奖励标识
					readHisVo.setUpdateDate(cTime);
					boolean flag2 =  this.readHisDao.updateReadHis(readHisVo);
					if (!flag2) {
						logger.error("阅读文章奖励标识更新异常", new RuntimeException("readHisId=" + readHisId));
					}
				}
				//4. 看是否需要给师傅奖励
				if(awardType == GlobalUtils.AWARD_TYPE_READ) {
					this.awardMasterHis(phoneNum, gold, awardType, cTime);
				}
				Map<String, Object> map = new HashMap<>();
				map.put("gold", gold);
				map.put("awardType", awardType);
				return new JsonResult(ResultCode.SUCCESS, map);
			}else {
				return new JsonResult(ResultCode.SUCCESS_FAIL);
			}
		} catch (Exception e) {
			logger.error("记录用户金币奖励记录异常：" + e.getMessage(), e);
			return new JsonResult(ResultCode.EXCEPTION);
		}
	}
	
	/**
	 * 随机阅读奖励，给师傅奖励
	 * @throws Exception
	 */
	public void awardMasterHis(String phoneNum, int gold, int awardType, String cTime) throws Exception {
		ShareUserHisVo shareUserHisVo = this.shareUserHisDao.selectShareUserByStudentPhoneNum(phoneNum);
		if(shareUserHisVo != null) {
			Map<String, Object> map = new HashMap<>();
			map.put("phoneNum", phoneNum);
			map.put("awardType", awardType);
			//收益总次数
			long awardCount = this.awardHisDao.selectUserAwardCount(map);
			StudentConfigVo studentConfigVo = this.studentConfigDao.selectStudentConfig(awardCount);
			if(studentConfigVo != null) {
				//5. 记录师傅的奖励明细
				int total = gold * studentConfigVo.getAlpha();
				AwardHisVo awardHisVo2 = new AwardHisVo();
				awardHisVo2.setId(UUID.getUUID());
				awardHisVo2.setPhoneNum(shareUserHisVo.getMasterPhoneNum());
				awardHisVo2.setAwardType(GlobalUtils.AWARD_TYPE_STUDENT);
				awardHisVo2.setAwardName(GlobalUtils.getAwardTypeName(GlobalUtils.AWARD_TYPE_STUDENT));
				awardHisVo2.setGold("+" + String.valueOf(total));
				awardHisVo2.setCreateDate(cTime);
				awardHisVo2.setUpdateDate(cTime);
				//6. 更新师傅的金币余额
				AppUserVo appUserVo2 = this.appUserDao.selectByPhoneNum(shareUserHisVo.getMasterPhoneNum());
				if(appUserVo2 != null) {
					int old2 = Integer.valueOf(appUserVo2.getGold());
					int sum2 = Integer.valueOf(appUserVo2.getGold()) + total;
					appUserVo2.setGold(String.valueOf(sum2));
					appUserVo2.setUpdateDate(cTime);
					logger.info("师傅的手机号码phoneNum=" + shareUserHisVo.getMasterPhoneNum() + ", 原金币余额gold=" + old2 + ", 奖励金币gold=" + gold 
							+ ", alpha值=" + studentConfigVo.getAlpha() + ", 总赠送金币=" + total 
							+ ", 更新金币总余额sum=" + sum2 + ", 奖励类型awardType=" + GlobalUtils.AWARD_TYPE_STUDENT);
					boolean flag2 =  this.awardHisDao.insertAwardHis(awardHisVo2);
					boolean flag3 = this.appUserDao.updateUser(appUserVo2);
					logger.info("赠送给师傅的金币更新结果，flag2=" + flag2 + ", flag3=" + flag3);
					
					if(flag2 && flag3) {
//						//7. 推送用户通知
//						String result2 =  XgServiceApi.pushNotifyByMessage(appUserVo2.getPhoneNum(), "恭喜获得奖励", 
//								 	"恭喜您获得徒弟阅读进贡奖励" + total + "金币", 
//									GlobalUtils.XG_ACCESS_ID, GlobalUtils.XG_SECRET_KEY);
//						logger.info("推送通知给用户[phoneNum=" + appUserVo2.getPhoneNum() + "]，推送发送结果result=" + result2);
					}
				}else {
					logger.warn("师傅账号phoneNum=" + shareUserHisVo.getMasterPhoneNum() + " 不存在。");
				}
			}
		}
	}
	
	public List<AwardHisVo> selectAwardHisByPhoneNum(String phoneNum) {
		return this.awardHisDao.selectAwardHisByPhoneNum(phoneNum);
	}
	
	public AwardHisVo selectAwardHisIsCheckIn(Map<String, Object> map) {
		return this.awardHisDao.selectAwardHisIsCheckIn(map);
	}
	
	public Long selectUserTotalOfGold(Map<String, Object> map) {
		return this.awardHisDao.selectUserTotalOfGold(map);
	}
	
	public Long selectUserAwardCount(Map<String, Object> map) {
		return this.awardHisDao.selectUserAwardCount(map);
	}
	
}
