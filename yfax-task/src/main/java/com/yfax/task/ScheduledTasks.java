package com.yfax.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yfax.task.cfdb.service.UserTaskListService;
import com.yfax.task.cfdb.vo.UserTaskListVo;
import com.yfax.task.qmtt.service.AppUserService;
import com.yfax.task.qmtt.service.BalanceHisService;
import com.yfax.task.qmtt.vo.AppUserVo;
import com.yfax.task.ytt.service.YttAppUserService;
import com.yfax.task.ytt.service.YttBalanceHisService;
import com.yfax.task.ytt.vo.YttAppUserVo;
import com.yfax.utils.DateUtil;
import com.yfax.utils.StrUtil;

@Component
public class ScheduledTasks {

	protected static Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

	@Autowired
	private AppUserService appUserService;
	@Autowired
	private BalanceHisService balanceHisService;
	@Autowired
	private UserTaskListService userTaskListService;
	
	@Autowired
	private YttAppUserService yttAppUserService;
	@Autowired
	private YttBalanceHisService yttBalanceHisService;

	// 以指定时间间隔调度任务（以方法执行开始时间为准）
//	@Scheduled(fixedRate = 20000)
//	public void reportCurrentTime() {
//		logger.info("======================start===================");
//		logger.info("my task is running, The time is now " + DateUtil.getCurrentLongDateTime());
//		this.batchResetDailyCheckIn();
//		this.batchAutoTransfer();
//		logger.info("=======================end====================");
//	}

	//每十分钟执行一次
	 @Scheduled(initialDelay = 1000, fixedDelay = 600000)
	 public void cfdbTask() {
		 logger.info("==================start=================");
		 logger.info("====返利达人任务，开始检查是否存在超时任务====");
		 List<UserTaskListVo> list = this.userTaskListService.selectUserTaskListByTime();
		 for (UserTaskListVo userTaskListVo : list) {
			this.userTaskListService.abandonUserTask(userTaskListVo.getId(), userTaskListVo.getPhoneId(), 
					userTaskListVo.getTaskId());
		 }
		 logger.info("==================end=================");
	 }

	// 指定延迟后开始调度任务
	// @Scheduled(initialDelay = 1000, fixedRate = 5000)
	// public void doSomething() {
	// // something that should execute periodically
	// }

	// cron表达式，second, minute, hour, day, month, weekday
	// 每日零点跑批
	@Scheduled(cron = "0 0/5 * * * *")
	public void qmttTask() {
		logger.info("============xxx任务，start===================");
		logger.info("xxx，my task is running, The time is now " + DateUtil.getCurrentLongDateTime());
		this.batchResetDailyCheckIn();
		this.batchAutoTransfer();
		logger.info("=======================end====================");
		
		logger.info("");
		try {
			logger.info("休眠三秒...");
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info("");
		
		logger.info("============xxx任务，start===================");
		logger.info("xxx，my task is running, The time is now " + DateUtil.getCurrentLongDateTime());
		this.batchResetDailyCheckInYtt();
		this.batchAutoTransferYtt();
		logger.info("=======================end====================");
	}
	
	//============xxx任务，start===================
	/**
	 * 每日零点重置用户今日签到标识
	 */
	private void batchResetDailyCheckIn() {
		logger.info("第一步，开始清除用户的每日签到标识...");
		List<AppUserVo> list = this.appUserService.selectAllUser();
		for (AppUserVo appUserVo : list) {
			if(appUserVo.getDailyCheckIn() == 1) {
				appUserVo.setDailyCheckIn(0);
				boolean result = this.appUserService.modifyUser(appUserVo);
				logger.info("phoneNum=" + appUserVo.getPhoneNum() 
					+ ", 重置结果result=" + (result?"成功":"失败"));
			}
		}
	}
	/**
	 * 跑批自动金币转零钱任务
	 */
	private void batchAutoTransfer() {
		logger.info("第二步，开始自动批量处理金币转换零钱...");
		List<AppUserVo> list = this.appUserService.selectByPhoneNumGoldLimit();
		for (AppUserVo appUserVo : list) {
			if(!StrUtil.null2Str(appUserVo.getGold()).equals("") 
					&& Integer.valueOf(appUserVo.getGold()) > 0) {
				logger.info("转换用户数据：" + appUserVo.toString());
				this.balanceHisService.addBalanceHis(appUserVo.getPhoneNum(), appUserVo.getGold());
			}
		}
	}
	//=======================end====================
	
	//============xxx任务，start===================
	/**
	 * 每日零点重置用户今日签到标识
	 */
	private void batchResetDailyCheckInYtt() {
		logger.info("第一步，开始清除用户的每日签到标识...");
		List<YttAppUserVo> list = this.yttAppUserService.selectAllUser();
		for (YttAppUserVo yttAppUserVo : list) {
			if(yttAppUserVo.getDailyCheckIn() == 1) {
				yttAppUserVo.setDailyCheckIn(0);
				boolean result = this.yttAppUserService.modifyUser(yttAppUserVo);
				logger.info("phoneNum=" + yttAppUserVo.getPhoneNum() 
					+ ", 重置结果result=" + (result?"成功":"失败"));
			}
		}
	}
	/**
	 * 跑批自动金币转零钱任务
	 */
	private void batchAutoTransferYtt() {
		logger.info("第二步，开始自动批量处理金币转换零钱...");
		List<YttAppUserVo> list = this.yttAppUserService.selectByPhoneNumGoldLimit();
		for (YttAppUserVo yttAppUserVo : list) {
			if(!StrUtil.null2Str(yttAppUserVo.getGold()).equals("") 
					&& Integer.valueOf(yttAppUserVo.getGold()) > 0) {
				logger.info("转换用户数据：" + yttAppUserVo.toString());
				this.yttBalanceHisService.addBalanceHis(yttAppUserVo.getPhoneNum(), yttAppUserVo.getGold());
			}
		}
	}
	//=======================end====================
}