package com.yfax.webapi.qmtt.dao;

import java.util.List;
import java.util.Map;

import com.yfax.webapi.qmtt.vo.ReadHisVo;

public interface ReadHisDao {
	public boolean insertReadHis(ReadHisVo readHisVo) throws Exception;
	public List<ReadHisVo> selectReadHisByPhoneNum(String phoneNum);
	public ReadHisVo selectReadHisByPhoneNumAndPrimaryKey(Map<String, Object> map);
}
