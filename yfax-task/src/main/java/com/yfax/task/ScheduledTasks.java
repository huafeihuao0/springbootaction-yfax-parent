package com.yfax.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yfax.task.service.AppUserService;
import com.yfax.task.service.BalanceHisService;
import com.yfax.task.vo.AppUserVo;
import com.yfax.utils.DateUtil;
import com.yfax.utils.StrUtil;

@Component
public class ScheduledTasks {

	protected static Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

	@Autowired
	private AppUserService appUserService;
	@Autowired
	private BalanceHisService balanceHisService;

	// 以指定时间间隔调度任务（以方法执行开始时间为准）
//	@Scheduled(fixedRate = 20000)
//	public void reportCurrentTime() {
//		this.myTask();
//	}

	// 以指定时间间隔调度（以方法执行结束时间为准）
	// @Scheduled(fixedDelay = 5000)
	// public void doSomething() {
	// // something that should execute periodically
	// }

	// 指定延迟后开始调度任务
	// @Scheduled(initialDelay = 1000, fixedRate = 5000)
	// public void doSomething() {
	// // something that should execute periodically
	// }

	// cron表达式，second, minute, hour, day, month, weekday
	//每日零点跑批
	@Scheduled(cron = "0 0 17 * * *")
	public void doSomething() {
		//TODO 配合APP测试，暂时改成每日下午5点跑批
		this.myTask();
	}
	
	/**
	 * 跑批任务
	 */
	private void myTask() {
		logger.info("my task is running, The time is now " + DateUtil.getCurrentLongDateTime());
		List<AppUserVo> list = this.appUserService.selectByPhoneNumGoldLimit();
		for (AppUserVo appUserVo : list) {
			if(!StrUtil.null2Str(appUserVo.getGold()).equals("") 
					&& Integer.valueOf(appUserVo.getGold()) > 0) {
				logger.info("转换用户数据：" + appUserVo.toString());
				this.balanceHisService.addBalanceHis(appUserVo.getPhoneNum(), appUserVo.getGold());
			}
		}
	}
}