package com.yfax.webapi.qmtt.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yfax.webapi.qmtt.dao.IpShareCodeDao;
import com.yfax.webapi.qmtt.vo.IpShareCodeVo;

/**
 * 记录邀请用户和IP对应关系
 * @author Minbo
 */
@Service
public class IpShareCodeService{
	
	protected static Logger logger = LoggerFactory.getLogger(IpShareCodeService.class);
	
	@Autowired
	private IpShareCodeDao dao;
	
	@Transactional
	public boolean addIpShareCode(IpShareCodeVo ipShareCodeVo){
		try {
			return this.dao.insertIpShareCode(ipShareCodeVo);
		} catch (Exception e) {
			logger.error("记录邀请用户和IP对应关系异常：" + e.getMessage(), e);
			return false;
		}
	}
	
	public IpShareCodeVo selectIpShareCodeByIp(String sourceIp) {
		return this.dao.selectIpShareCodeByIp(sourceIp);
	}
	
}
