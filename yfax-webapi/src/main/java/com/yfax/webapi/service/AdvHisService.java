package com.yfax.webapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.webapi.dao.AdvHisDao;
import com.yfax.webapi.dao.IncomeHisDao;
import com.yfax.webapi.dao.SdkTasklistDao;
import com.yfax.webapi.dao.UsersDao;
import com.yfax.webapi.utils.DateUtil;
import com.yfax.webapi.utils.UUID;
import com.yfax.webapi.vo.AdvHisVo;
import com.yfax.webapi.vo.IncomeHisVo;
import com.yfax.webapi.vo.SdkTasklistVo;
import com.yfax.webapi.vo.UsersVo;
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
	
	/**
	 * 积分兑换比率，1比100
	 */
	private static final int POINT_RATE = 100;
	
	/**
	 * 新增广告平台回调记录
	 * @return
	 */
	@Transactional
	public boolean addAdvHis(AdvHisVo advHis) {
		try {
			//1. 新增广告平台回调记录
			boolean flag1 = this.advHisDao.insertAdvHis(advHis);
			if(flag1) {
				logger.info("新增广告平台回调记录成功。");
				this.addUserBalance(advHis);
				return true;
			}else {
				logger.error("新增广告平台回调记录失败。");
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
	private void addUserBalance(AdvHisVo advHis) throws Exception {
		//2. 增加用户余额，1比100
		UsersVo usersVo = this.usersDao.selectUsersByPhoneId(advHis.getDeviceid());	//IMEI手机码
		
		if(usersVo != null) {
			//更新数据
			int balance = Integer.valueOf(usersVo.getBalance());	//原已有余额
			int totalIncome = Integer.valueOf(usersVo.getTotalIncome());	//原累积收入
			int point = Integer.valueOf(advHis.getPoint());	//本次获赠积分
			int earn = point / POINT_RATE;	//本次获得的钱
			usersVo.setBalance(String.valueOf(balance + earn));
			usersVo.setTotalIncome(String.valueOf(totalIncome + earn));
			usersVo.setUpdateDate(DateUtil.getCurrentLongDateTime());
			
			//3. 记录用户收益记录
			SdkTasklistVo sdkTasklistVo = this.sdkTasklistDao.selectSdkTasklistByAdid(advHis.getAdid());
			if(sdkTasklistVo != null) {
				IncomeHisVo incomeHis = new IncomeHisVo();
				String cTime = DateUtil.getCurrentLongDateTime();
				incomeHis.setId(UUID.getUUID());
				incomeHis.setPhoneId(advHis.getDeviceid());
				incomeHis.setTaskId(advHis.getAdid());
				incomeHis.setLogoUrl(sdkTasklistVo.getIcon());
				incomeHis.setTaskName(sdkTasklistVo.getTitle());
				incomeHis.setIncomeTime(cTime);
				incomeHis.setIncome(String.valueOf(earn));
				incomeHis.setCreateDate(cTime);
				incomeHis.setUpdateDate(cTime);
				incomeHis.setFlag(1);	//1=加钱；2=扣钱
				incomeHis.setChannel(2);		//1=后台系统广告；2=SDK平台广告
				
				//仅限第一次记录，重复则跳过
				IncomeHisVo isExistVo = this.incomeHisDao.selectIncomeHisByCondition(incomeHis);
				if(isExistVo == null) {
					boolean flag = this.incomeHisDao.insertIncomeHis(incomeHis);			//记录用户收益记录情况
					if(flag) {
						logger.info("记录用户收益成功。phoneId=" + advHis.getDeviceid() 
							+ ", taskName=" + sdkTasklistVo.getTitle() + ", earn=" + earn);
						
						//4. 更新用户余额数据
						boolean flag2 = usersDao.updateUser(usersVo);
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
					logger.warn("该用户已记录过收益数据，跳过处理。taskId=" + incomeHis.getTaskId() 
							+ ", phoneId=" + incomeHis.getPhoneId());
				}
				
			}else {
				logger.warn("SDK平台广告数据不存在。adi=" + advHis.getAdid());
			}
			
		}else {
			logger.warn("IMEI手机码数据不存在。phoneId=" + advHis.getDeviceid());
		}
	}
}
