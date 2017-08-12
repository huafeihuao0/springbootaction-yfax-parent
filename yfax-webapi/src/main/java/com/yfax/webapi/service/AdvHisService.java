package com.yfax.webapi.service;

import java.text.DecimalFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.webapi.GlobalUtils;
import com.yfax.webapi.cfdb.vo.AdvHisVo;
import com.yfax.webapi.cfdb.vo.IncomeHisVo;
import com.yfax.webapi.cfdb.vo.SdkChannelConfigVo;
import com.yfax.webapi.cfdb.vo.SdkTasklistVo;
import com.yfax.webapi.cfdb.vo.UsersVo;
import com.yfax.webapi.dao.AdvHisDao;
import com.yfax.webapi.dao.IncomeHisDao;
import com.yfax.webapi.dao.SdkChannelConfigDao;
import com.yfax.webapi.dao.SdkTasklistDao;
import com.yfax.webapi.dao.UsersDao;
import com.yfax.webapi.utils.DateUtil;
import com.yfax.webapi.utils.UUID;
import com.yfax.webapi.xinge.XgServiceApi;

/**
 * 广告平台回调记录
 * @author Minbo
 */
@Service
public class AdvHisService{
	
	protected static Logger logger = LoggerFactory.getLogger(AdvHisService.class);
	
	@Autowired
	private AdvHisDao advHisDao;
	
	@Autowired
	private UsersDao usersDao;
	
	@Autowired
	private SdkTasklistDao sdkTasklistDao;
	
	@Autowired
	private IncomeHisDao incomeHisDao;
	
	@Autowired
	private SdkChannelConfigDao sdkChannelConfigDao;
	
	/**
	 * 新增广告平台回调记录
	 * @return
	 */
	@Transactional
	public boolean addAdvHis(AdvHisVo advHis, int channelFlag) {
		try {
			//1. 新增广告平台回调记录
			AdvHisVo advHisTmp = this.advHisDao.selectByHashid(advHis.getHashid());
			if(advHisTmp != null) {
				logger.info("广告平台回调记录已存在，跳过处理");
				return false;
			}
			boolean flag1 = this.advHisDao.insertAdvHis(advHis);
			if(flag1) {
				logger.info("新增广告平台回调记录成功");
				this.addUserBalance(advHis, channelFlag);
				return true;
			}else {
				logger.error("新增广告平台回调记录失败");
				return false;
			}
		} catch (Exception e) {
			logger.error("新增广告平台回调记录异常：" + e.getMessage(), e);
			return false;
		}
	}
	
	/**
	 * 增加用户余额
	 * @param advHis
	 * @throws Exception 
	 */
	@Transactional
	private void addUserBalance(AdvHisVo advHis, int channelFlag) throws Exception {
		//2. 取用户余额
		UsersVo usersVo = this.usersDao.selectUsersByPhoneId(advHis.getDeviceid());	//IMEI手机码
		String sdkChannelFlag = "";
		if(channelFlag == 4) {
			sdkChannelFlag = GlobalUtils.DIANRU_CN;
		}else if(channelFlag == 5) {
			sdkChannelFlag = GlobalUtils.YOUMI_CN;
		}
		//3. 取对应渠道的换算和分成比例
		SdkChannelConfigVo sdkChannelConfigVo = this.sdkChannelConfigDao.selectSdkChannelConfigByFlag(sdkChannelFlag);
		//初始默认值
		double cRate = 0;
		double sRate = 0;
		if(sdkChannelConfigVo != null) {
			cRate = Double.valueOf(sdkChannelConfigVo.getcRate());	//换算比例
			sRate = Double.valueOf(sdkChannelConfigVo.getsRate());	//分成比例
		}
		if(usersVo != null) {
			//格式化，保留两位小数，四舍五入
			DecimalFormat dFormat = new DecimalFormat(GlobalUtils.DECIMAL_FORMAT); 
			//用户原已有余额
			double balance = Double.valueOf(usersVo.getBalance());
			//用户原累积收入
			double totalIncome = Double.valueOf(usersVo.getTotalIncome());	
			//平台返赠总积分
			double point = Double.valueOf(advHis.getPoint());		
			//用户本次收益金额
			double earn = (point/cRate)*(sRate/100);
			usersVo.setBalance(dFormat.format(balance + earn));
			usersVo.setTotalIncome(dFormat.format(totalIncome + earn));
			//平台本次收益金额
			double sysEarn = (point/cRate)*((100-sRate)/100);
			//检查是否存在该广告
			SdkTasklistVo sdkTasklistVo = this.sdkTasklistDao.selectSdkTasklistByAdid(advHis.getAdid());
			if(sdkTasklistVo != null) {
				String cTime = DateUtil.getCurrentLongDateTime();
				//3. 记录用户收益记录
				IncomeHisVo incomeHis = new IncomeHisVo();
				incomeHis.setId(UUID.getUUID());
				incomeHis.setPhoneId(advHis.getDeviceid());
				incomeHis.setTaskId(advHis.getAdid());
				incomeHis.setLogoUrl(sdkTasklistVo.getIcon());
				incomeHis.setTaskName(sdkTasklistVo.getTitle());
				incomeHis.setIncomeTime(cTime);
				incomeHis.setIncome(dFormat.format(earn));		//用户收益金额
				incomeHis.setSysIncome(dFormat.format(sysEarn));	//平台收益金额
				incomeHis.setCreateDate(cTime);
				incomeHis.setUpdateDate(cTime);
				incomeHis.setFlag(1);	//1=加钱；2=扣钱
				incomeHis.setChannel(channelFlag);
				
				//仅限第一次记录，重复则跳过
				IncomeHisVo isExistVo = this.incomeHisDao.selectIncomeHisByCondition(incomeHis);
				if(isExistVo == null) {
					boolean flag = this.incomeHisDao.insertIncomeHis(incomeHis);			//记录用户收益记录情况
					if(flag) {
						logger.info("记录用户收益成功。phoneId=" + advHis.getDeviceid() 
							+ ", taskName=" + sdkTasklistVo.getTitle() + ", earn=" + earn);
						
						//4. 更新用户余额数据
						usersVo.setUpdateDate(cTime);
						boolean flag2 = usersDao.update(usersVo);
						if(flag2) {
							logger.info("回调用户加钱成功。balance=" + balance 
									+ ", totalIncome=" + totalIncome 
									+ ", point=" + point + ", earn=" + earn);
							
							//推送用户通知
							String result =  XgServiceApi.pushNotifyByMessage(advHis.getDeviceid(), "恭喜获得奖励", 
									"任务[" + incomeHis.getTaskName() + "]已完成，获得收益：" + incomeHis.getIncome() + "元");
							logger.info("推送通知给用户[phoneId=" + advHis.getDeviceid() + "]，推送发送结果result=" + result);
						}else {
							logger.warn("回调用户加钱失败。balance=" + balance 
									+ ", totalIncome=" + totalIncome 
									+ ", point=" + point + ", earn=" + earn);
						}
					}else {
						logger.warn("记录用户收益失败。phoneId=" + advHis.getDeviceid() 
							+ ", taskName=" + sdkTasklistVo.getTitle() + ", earn=" + earn);
					}
				}else {
					logger.warn("用户该收益数据已存在，跳过处理。taskId=" + incomeHis.getTaskId() 
							+ ", phoneId=" + incomeHis.getPhoneId());
				}
				
			}else {
				logger.warn("SDK平台广告数据不存在。adid=" + advHis.getAdid());
			}
			
		}else {
			logger.warn("IMEI手机码数据不存在。phoneId=" + advHis.getDeviceid());
		}
	}
	
//	public static void main(String[] args) {
//		DecimalFormat dFormat = new DecimalFormat(GlobalUtils.DECIMAL_FORMAT); 
//		double cRate = 50;	//换算比例
//		double sRate = 10;	//分成比例
//		double point = Double.valueOf("100");	//本次获赠积分
//		//用户收益
//		double userPoint = (sRate/100)*point;	//用户本次获得积分
//		double earn = (point/cRate)*(sRate/100);	//用户本次收益金额
//		System.out.println("userPoint=" + userPoint);
//		System.out.println("earn=" + earn);
//		//平台本次收益金额
//		System.out.println("sysEarn=" + (point/cRate)*((100-sRate)/100));
//	}
}
