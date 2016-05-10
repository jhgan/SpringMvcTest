package test.mvc.spring.service;

import javax.servlet.http.HttpServletRequest;

public interface SocialService {
	public String getOauthUrlBySocialType(HttpServletRequest request, String socialType);
	public String getUserInfo(HttpServletRequest request, String socialType, String code, String state);
}
