package test.mvc.spring.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import test.mvc.spring.service.impl.SocialServiceImpl;

@Controller
@RequestMapping(value = "/social")
public class SocialController {
	@Autowired
	private SocialServiceImpl socialServiceImpl;
	
	@RequestMapping(value = "/oauth/{socialType}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> returnOauthUrlBySocialType(HttpServletRequest request, @PathVariable("socialType") String socialType) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("url", socialServiceImpl.getOauthUrlBySocialType(request, socialType));
		return param;
	}
	
	@RequestMapping(value = "/{socialType}/callback")
	public String getSocialCallback(HttpServletRequest request, @PathVariable("socialType") String socialType, String code, String state) {
		return socialServiceImpl.getUserInfo(request, socialType, code, state);
	}
}
