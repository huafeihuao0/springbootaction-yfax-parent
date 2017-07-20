package com.yfax.webapi.web.mybatis.xml;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HotelMapper {
	Hotel selectByCityId(int city_id);
}
