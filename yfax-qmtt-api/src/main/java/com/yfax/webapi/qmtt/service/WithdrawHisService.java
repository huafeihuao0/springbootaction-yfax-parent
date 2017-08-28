package com.yfax.webapi.qmtt.service;

import java.text.DecimalFormat;
import java.util.List;

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
import com.yfax.webapi.qmtt.dao.AppUserDao;
import com.yfax.webapi.qmtt.dao.BalanceHisDao;
import com.yfax.webapi.qmtt.dao.WithdrawHisDao;
import com.yfax.webapi.qmtt.vo.AppUserVo;
import com.yfax.webapi.qmtt.vo.BalanceHisVo;
import com.yfax.webapi.qmtt.vo.WithdrawHisVo;

/**
 * 兑换提现记录
 * @author Minbo
 */
@Service
public class WithdrawHisService{
	
	protected static Logger logger = LoggerFactory.getLogger(WithdrawHisService.class);
	
	@Autowired
	private WithdrawHisDao withdrawHisDao;
	
	@Autowired
	private AppUserDao appUserDao;
	
	@Autowired
	private BalanceHisDao balanceHisDao;
	
	/**
	 * 查询指定用户名下的提现记录
	 * @param phoneId
	 * @return
	 */
	public List<WithdrawHisVo> selectWithdrawHis(String phoneId){
		return this.withdrawHisDao.selectWithdrawHis(phoneId);
	}
	
	/**
	 * 新增提现记录
	 * @param phoneId
	 * @param withdrawType
	 * @param name
	 * @param account
	 * @param income
	 * @return
	 */
	@Transactional
	public JsonResult addWithdrawHis(String phoneNum, int withdrawType, 
			String name, String account, String income) {
		//1. 新增提现记录
		WithdrawHisVo withdrawHisVo = new WithdrawHisVo();
		withdrawHisVo.setId(UUID.getUUID());
		withdrawHisVo.setPhoneNum(phoneNum);
		withdrawHisVo.setWithdrawType(withdrawType);
		withdrawHisVo.setWithdrawName(getWithdrawName(withdrawType));
		withdrawHisVo.setName(name);
		withdrawHisVo.setAccount(account);
		withdrawHisVo.setIncome("+"+income);
		String cTime = DateUtil.getCurrentLongDateTime();
		withdrawHisVo.setWithdrawTime(cTime);
		withdrawHisVo.setStatus("处理中");
		withdrawHisVo.setStatusType(1);
		withdrawHisVo.setCreateDate(cTime);
		withdrawHisVo.setUpdateDate(cTime);
		try {
			//2. 记录零钱兑换扣减记录
			BalanceHisVo balanceHisVo = new BalanceHisVo();
			balanceHisVo.setId(UUID.getUUID());
			balanceHisVo.setPhoneNum(phoneNum);
			balanceHisVo.setBalanceType(GlobalUtils.BALANCE_TYPE_WITHDRAW);
			balanceHisVo.setBalanceName("提现申请");
			balanceHisVo.setBalance("-"+income);
			balanceHisVo.setCreateDate(cTime);
			balanceHisVo.setUpdateDate(cTime);
			//3. 提现，则需要扣减用户余额
			AppUserVo appUserVo = this.appUserDao.selectByPhoneNum(withdrawHisVo.getPhoneNum());
			//格式化，保留三位小数，四舍五入
			DecimalFormat dFormat = new DecimalFormat(GlobalUtils.DECIMAL_FORMAT); 
			//更新数据
			double balance = Double.valueOf(appUserVo.getBalance());	//原已有余额
			double incomeTmp = Double.valueOf(income);	//提现金额
			appUserVo.setUpdateDate(cTime);
			appUserVo.setBalance(dFormat.format(balance-incomeTmp));
			logger.info("用户提现申请后，原零钱余额balance=" + balance + ", 提现金额incomeTmp=" + incomeTmp 
					+ "，更新后余额=" + dFormat.format(balance-incomeTmp));
			
			boolean flag = this.withdrawHisDao.insertWithdrawHis(withdrawHisVo);
			boolean flag1 = this.balanceHisDao.insertBalanceHis(balanceHisVo);
			boolean flag2 = this.appUserDao.updateUser(appUserVo);
			if(flag && flag1 && flag2) {
				return new JsonResult(ResultCode.SUCCESS);
			}else {
				return new JsonResult(ResultCode.SUCCESS_NO_USER);
			}
		} catch (Exception e) {
			logger.error("用户提现申请异常：" + e.getMessage(), e);
			return new JsonResult(ResultCode.EXCEPTION);
		}
	}
	
	/**
	 * 解析提现名称
	 * @param withdrawType
	 * 1=手机充值;2=支付宝;3=微信;4=QQ币
	 */
	private static String getWithdrawName(int withdrawType) {
		switch (withdrawType) {
			case 1:
				return "手机充值";
			case 2:
				return "支付宝";
			case 3:
				return "微信";
			case 4:
				return "QQ币";
			default:
				throw new RuntimeException("未知提现类型 withdrawType值");
		}
	}

}
