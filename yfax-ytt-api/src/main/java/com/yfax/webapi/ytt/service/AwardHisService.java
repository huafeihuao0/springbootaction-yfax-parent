package com.yfax.webapi.ytt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.utils.DateUtil;
import com.yfax.utils.JsonResult;
import com.yfax.utils.ResultCode;
import com.yfax.utils.UUID;
import com.yfax.webapi.GlobalUtils;
import com.yfax.webapi.ytt.dao.AppConfigDao;
import com.yfax.webapi.ytt.dao.AppUserDao;
import com.yfax.webapi.ytt.dao.AwardHisDao;
import com.yfax.webapi.ytt.dao.ReadHisDao;
import com.yfax.webapi.ytt.dao.ShareUserHisDao;
import com.yfax.webapi.ytt.dao.StudentConfigDao;
import com.yfax.webapi.ytt.vo.AppConfigVo;
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
	@Autowired
	private AppConfigDao appConfigDao;
	
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
	
	public List<AwardHisVo> selectAwardHisCheckList(String phoneNum) {
		return this.awardHisDao.selectAwardHisCheckList(phoneNum);
	}
	
	/**
	 * 根据奖励记录获取七日签到数据
	 * @param phoneNum
	 * @return
	 */
	public JsonResult queryContinueCheckList(String phoneNum) {
		Map<String, Object> resultMap = new HashMap<>();
		List<AwardHisVo> list = this.awardHisDao.selectAwardHisCheckList(phoneNum);
		List<Map<String, Object>> mapList = new ArrayList<>();
		if(list != null) {
			Map<String, Object> map = new HashMap<>();
			String currentDate = DateUtil.getCurrentDate();
			int day = DateUtil.getWeek(DateUtil.parseDate(currentDate));
			for (int i = 1; i <day; i++) {
				map = new HashMap<>();
				map.put("day", i);
				String preDate = DateUtil.formatDate(DateUtils.addDays(DateUtil.parseDate(currentDate), -i));
				map.put("isCheckIn", getCheckInFlag(preDate, list));
				mapList.add(map);
			}
			int todayCheckIn = getCheckInFlag(currentDate, list);
			map = new HashMap<>();
			map.put("day", day);
			map.put("isCheckIn", todayCheckIn);
			resultMap.put("gold", todayCheckIn==0?this.calAwardGold(phoneNum):0);
			mapList.add(map);
			for (int i = day+1; i < 8; i++) {
				map = new HashMap<>();
				map.put("day", i);
				map.put("isCheckIn", 0);
				mapList.add(map);
			}
		}
		resultMap.put("list", mapList);
		return new JsonResult(ResultCode.SUCCESS, resultMap);
	}
	
	/**
	 * 计算预期获得金币值
	 * @param phoneNum
	 * @return 可预期获得金币值
	 */
	public int calAwardGold(String phoneNum) {
		//用户数据
		AppUserVo appUserVo = this.appUserDao.selectByPhoneNum(phoneNum);
		//配置信息
		AppConfigVo appConfigVo = this.appConfigDao.selectAppConfig();
		//随机金币奖励
		int gold = GlobalUtils.getRanomGold(appConfigVo.getCheckInGold());
		String checkInConfig = appConfigVo.getCheckInConfig();
		int userGold = Integer.valueOf(appUserVo.getGold());
		//最终获得金币
		int finalGlod = GlobalUtils.getContinueCheckInGold(gold, userGold, checkInConfig);
		logger.info("phoneNum=" + phoneNum + ", userGold=" + userGold + ", 获得随机金币gold=" + gold
				+ ", 获得金币glod=" + finalGlod + ", 最终余额result=" + (userGold + finalGlod));
		return finalGlod;
	}
	
	/**
	 * 通过签到奖励数据看是否已签到过
	 * @param date
	 * @param list
	 * @return 1=已签到；0=未签到
	 */
	private int getCheckInFlag(String date, List<AwardHisVo> list) {
		for (AwardHisVo awardHisVo : list) {
			String createDate = awardHisVo.getCreateDate().substring(0, 10);
			if(createDate.equals(date)) {
				return 1;
			}
		}
		return 0;
	}
}
