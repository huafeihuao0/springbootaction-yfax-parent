package com.yfax.webapi.dao;

import com.yfax.webapi.cfdb.vo.AdvHisVo;

public interface AdvHisDao {
	public boolean insertAdvHis(AdvHisVo advHis) throws Exception;
	public AdvHisVo selectByHashid(String hashid);
}
