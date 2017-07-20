package com.yfax.webapi.web.mybatis.xml;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Minbo.He
 */
@Component
public class CityDao {

//	@Autowired
//	private SqlSession sqlSession;
//
//	public int addCity(City city) {
//		return this.sqlSession.insert("insertXmlCity", city);
//	}
//	
//	public int removeCityById(long id) {
//		return this.sqlSession.delete("deleteXmlCityById", id);
//	}
//	
//	public City selectCityById(long id) {
//		return this.sqlSession.selectOne("selectXmlCityById", id);
//	}
//	
//	public List<City> selectCityList() {
//		return this.sqlSession.selectList("selectCityList");
//	}
//	
//	public int modifyCityById(City city) {
//		return this.sqlSession.update("updateXmlCityById", city);
//	}
}
