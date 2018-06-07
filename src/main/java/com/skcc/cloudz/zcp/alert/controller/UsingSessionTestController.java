package com.skcc.cloudz.zcp.alert.controller;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UsingSessionTestController {
	
	@Resource(name="redisTemplate")
	private RedisTemplate<String, Object> redisTemplate;
	
	@ResponseBody
	@RequestMapping(path="/session-test", produces="text/plain")
	public String sessionTest(HttpSession session)
	{
		session.setAttribute("test", "hello");
		return (String)session.getAttribute("test");
	}
	
}
