package com.yfax.webapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yfax.webapi.dao.AdvHisDao;
import com.yfax.webapi.dao.IncomeHisDao;
import com.yfax.webapi.dao.TaskListDao;
import com.yfax.webapi.dao.UsersDao;
import com.yfax.webapi.utils.DateUtil;
import com.yfax.webapi.vo.AdvHisVo;
import com.yfax.webapi.vo.UsersVo;

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
	
//	@Autowired
//	private TaskListDao taskListDao;
//	
//	@Autowired
//	private IncomeHisDao incomeHisDao;
	
	/**
	 * 积分兑换比率，1比100
	 */
	private static final int POINT_RATE = 100;
	
	/**
	 * 新增广告平台回调记录
	 * @return
	 */
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
	 */
	private void addUserBalance(AdvHisVo advHis) {
		try {
			//2. 增加用户余额，1比100
			UsersVo usersVo = this.usersDao.selectUsersByPhoneId(advHis.getDeviceid());
			//更新数据
			int balance = Integer.valueOf(usersVo.getBalance());	//原已有余额
			int point = Integer.valueOf(advHis.getPoint());	//本次获赠积分
			int earn = point / POINT_RATE;	//本次获得的钱
			usersVo.setBalance(String.valueOf(balance + earn));
			usersVo.setUpdateDate(DateUtil.getCurrentLongDateTime());
			boolean flag2 = usersDao.updateUser(usersVo);
			if(flag2) {
				logger.info("用户加钱成功。");
			}else {
				logger.error("用户加钱失败。");
			}
			
			//TODO 新增用户收益记录
		} catch (Exception e) {
			logger.error("用户加钱失败异常：" + e.getMessage(), e);
		}
	}
}
