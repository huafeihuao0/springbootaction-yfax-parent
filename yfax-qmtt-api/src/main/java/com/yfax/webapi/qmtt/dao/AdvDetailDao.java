package com.yfax.webapi.qmtt.dao;

import java.util.List;

import com.yfax.webapi.qmtt.vo.AdvDetailVo;

public interface AdvDetailDao {
	
	public List<AdvDetailVo> selectAdvDetail(String advFkid);
	
}
