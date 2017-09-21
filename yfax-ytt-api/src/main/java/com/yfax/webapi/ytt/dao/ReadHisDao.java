package com.yfax.webapi.ytt.dao;

import java.util.List;
import java.util.Map;

import com.yfax.webapi.ytt.vo.ReadHisVo;

public interface ReadHisDao {
	public boolean insertReadHis(ReadHisVo readHisVo) throws Exception;
	public boolean updateReadHis(ReadHisVo readHisVo) throws Exception;
	public List<ReadHisVo> selectReadHisByPhoneNum(String phoneNum);
	public Long selectReadHisCountByPhoneNum(Map<String, Object> map);
	public ReadHisVo selectReadHisById(String id);
	public ReadHisVo selectReadHisByPhoneNumAndPrimaryKey(Map<String, Object> map);
}
