package com.yfax.webapi.web.mybatis.xml;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yfax.webapi.utils.JsonResult;
import com.yfax.webapi.utils.ResultCode;

/**
 * SpringBoot与Mybatis 
 * 使用xml方式集成
 * 
 * @author Minbo.He
 */
@RestController
@RequestMapping("/mybatis/xml")
public class ControllerTestForMybatisXml {

//	protected static Logger logger = LoggerFactory.getLogger(ControllerTestForMybatisXml.class);
//
//	@Autowired
//	private CityDao cityDao;
//
//	@Autowired
//	private HotelMapper hotelMapper;
//
//	@RequestMapping("/")
//	public String greeting() {
//		return "Greetings from Spring Mybatis Xml!";
//	}
//	
//	@RequestMapping("/cityAll")
//	public JsonResult City() {
//		List city = this.cityDao.selectCityList();
////		result.setOK(ResultCode.CODE_STATE_200, "", city);
////		logger.info("city=" + city);
////		return result.toJsonString();
//		return new JsonResult(ResultCode.SUCCESS, "登录成功！", city);
//	}
//
//	@RequestMapping("/city")
//	public String City1() {
//		String city = this.cityDao.selectCityById(1).toString();
//		logger.info("city=" + city);
//		return city;
//	}
//
//	@RequestMapping("/hotel")
//	public String getCity2() {
//		String hotel = this.hotelMapper.selectByCityId(1).toString();
//		logger.info("hotel=" + hotel);
//		return hotel;
//	}
//	
//	//调用例如：http://localhost:8080/mybatis/xml/addCity?name=hangzhou&state=HZ&country=CH
//	@RequestMapping("/addCity")
//	public String addCity(String name, String state, String country) {
//		City city = new City();
//		city.setName(name);
//		city.setState(state);
//		city.setCountry(country);
//		int result = this.cityDao.addCity(city);
//		return this.result(result);
//	}
//
//	//调用例如：http://localhost:8080/mybatis/xml/removeCity?id=2
//	@RequestMapping("/removeCity")
//	public String removeCity(long id) {
//		int result = this.cityDao.removeCityById(id);
//		return this.result(result);
//	}
//	
//	//调用例如：http://localhost:8080/mybatis/xml/modifyCity?id=6&name=guangzhou&state=GZ&country=CH
//	@RequestMapping("/modifyCity")
//	public String modifyCity(long id, String name, String state, String country) {
//		City city = new City();
//		city.setId(id);
//		city.setName(name);
//		city.setState(state);
//		city.setCountry(country);
//		int result = this.cityDao.modifyCityById(city);
//		return this.result(result);
//	}
//	
//	private String result(int result){
//		logger.info("result=" + result);
//		if(result==1){
//			return "success";
//		}else if(result==0){
//			return "failure";
//		}
//		return "unknown result";
//	}
}