package com.yfax.webapi.qmtt.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yfax.webapi.qmtt.dao.ReadHisDao;
import com.yfax.webapi.qmtt.vo.ReadHisVo;

/**
 * 用户阅读文章历史
 * @author Minbo
 */
@Service
public class ReadHisService{
	
	protected static Logger logger = LoggerFactory.getLogger(ReadHisService.class);
	
	@Autowired
	private ReadHisDao readHisDao;
	
	@Transactional
	public boolean addReadHis(ReadHisVo readHisVo){
		try {
			return this.readHisDao.insertReadHis(readHisVo);
		} catch (Exception e) {
			logger.error("新增用户阅读文章历史异常：" + e.getMessage(), e);
			return false;
		}
	}
	
	@Transactional
	public boolean modifyReadHis(ReadHisVo readHisVo){
		try {
			return this.readHisDao.updateReadHis(readHisVo);
		} catch (Exception e) {
			logger.error("修改用户阅读文章历史异常：" + e.getMessage(), e);
			return false;
		}
	}
	
	public List<ReadHisVo> selectReadHisByPhoneNum(String phoneNum) {
		return this.readHisDao.selectReadHisByPhoneNum(phoneNum);
	}
	
	public Long selectCountByPhoneNum(Map<String, Object> map) {
		return this.readHisDao.selectReadHisCountByPhoneNum(map);
	}
	
	public ReadHisVo selectReadHisById(String id){
		return this.readHisDao.selectReadHisById(id);
	}
	
	public ReadHisVo selectReadHisByPhoneNumAndPrimaryKey(Map<String, Object> map){
		return this.readHisDao.selectReadHisByPhoneNumAndPrimaryKey(map);
	}
	
}
