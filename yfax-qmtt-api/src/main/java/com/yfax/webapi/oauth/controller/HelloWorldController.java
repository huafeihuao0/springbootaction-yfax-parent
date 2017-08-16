package com.yfax.webapi.oauth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yfax.webapi.GlobalUtils;

@RestController
@RequestMapping("/hello")
public class HelloWorldController {

    @RequestMapping(method = RequestMethod.GET)
    public String sayHello() {
        return "Hello，Greetings from 开放Api平台（" + GlobalUtils.PROJECT_QMTT + "）!!!";
    }

}
