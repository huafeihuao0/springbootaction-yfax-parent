package com.yfax.webapi.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yfax.webapi.dao.WithdrawHisDao;
import com.yfax.webapi.utils.DateUtil;
import com.yfax.webapi.utils.JsonResult;
import com.yfax.webapi.utils.ResultCode;
import com.yfax.webapi.utils.UUID;
import com.yfax.webapi.vo.WithdrawHisVo;

/**
 * 兑换提现记录
 * @author Minbo
 */
@Service
public class WithdrawHisService{
	
	protected static Logger logger = LoggerFactory.getLogger(WithdrawHisService.class);
	
	@Autowired
	private WithdrawHisDao dao;
	
	/**
	 * 查询指定用户名下的提现记录
	 * @param phoneId
	 * @return
	 */
	public List<WithdrawHisVo> selectWithdrawHis(String phoneId){
		return this.dao.selectWithdrawHis(phoneId);
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
	public JsonResult addWithdrawHis(String phoneId, int withdrawType, 
			String name, String account, String income) {
		WithdrawHisVo withdrawHisVo = new WithdrawHisVo();
		withdrawHisVo.setId(UUID.getUUID());
		withdrawHisVo.setPhoneId(phoneId);
		withdrawHisVo.setWithdrawType(withdrawType);
		withdrawHisVo.setWithdrawName(getWithdrawName(withdrawType));
		withdrawHisVo.setName(name);
		withdrawHisVo.setAccount(account);
		withdrawHisVo.setIncome(income);
		String cTime = DateUtil.getCurrentLongDateTime();
		withdrawHisVo.setWithdrawTime(cTime);
		withdrawHisVo.setStatus("处理中");
		withdrawHisVo.setStatusType(1);
		withdrawHisVo.setCreateDate(cTime);
		withdrawHisVo.setUpdateDate(cTime);
		try {
			boolean flag = this.dao.insertWithdrawHis(withdrawHisVo);
			if (flag) {
				return new JsonResult(ResultCode.SUCCESS);
			}else {
				return new JsonResult(ResultCode.SUCCESS_FAIL);
			}
		} catch (Exception e) {
			logger.error("新增提现记录异常：" + e.getMessage(), e);
			return new JsonResult(ResultCode.EXCEPTION);
		}
	}
	
	/**
	 * 解析提现名称
	 * @param withdrawType
	 * 1=手机充值;2=支付宝;3=微信
	 */
	private static String getWithdrawName(int withdrawType) {
		switch (withdrawType) {
			case 1:
				return "手机充值";
			case 2:
				return "支付宝";
			case 3:
				return "微信";
			default:
				throw new RuntimeException("未知提现类型 withdrawType值");
		}
	}

}
