package com.yfax.webapi.ytt.dao;

import java.util.List;
import java.util.Map;

import com.yfax.webapi.ytt.vo.AdvDetailVo;

public interface AdvDetailDao {
	
	public List<AdvDetailVo> selectAdvDetail(Map<String, Object> map);
	
}
