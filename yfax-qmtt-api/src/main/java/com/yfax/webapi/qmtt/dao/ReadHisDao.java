package com.yfax.webapi.qmtt.dao;

import java.util.List;
import java.util.Map;

import com.yfax.webapi.qmtt.vo.ReadHisVo;

public interface ReadHisDao {
	public boolean insertReadHis(ReadHisVo readHisVo) throws Exception;
	public List<ReadHisVo> selectReadHisByPhoneNum(String phoneNum);
	public Long selectCountByPhoneNum(Map<String, Object> map);
	public Long selectCountByPhoneNumAndPrimaryKey(Map<String, Object> map);
}
