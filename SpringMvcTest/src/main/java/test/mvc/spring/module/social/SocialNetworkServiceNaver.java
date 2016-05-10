package test.mvc.spring.module.social;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocialNetworkServiceNaver extends AbstractSocialNetworkService {
	private static final Logger logger = LoggerFactory.getLogger(SocialNetworkServiceNaver.class);
	
	private static final String NAVER_AUTH_HOST = "https://nid.naver.com";
	private static final String NAVER_API_HOST = "https://openapi.naver.com";
	private static final String NAVER_CLIENT_KEY = "yARxDvyeS0nTwisjXTHJ";
	private static final String NAVER_CLIENT_SECRET = "DItYX6E4Qg";
	private static final String NAVER_CLIENT_CALLBACK = "/social/naver/callback";
	
	public String createOAuthAuthorizationURL(String redirectUri, String state) {
//		return DAUM_HOST + "/oauth2/authorize?client_id=" + DAUM_CLIENT_KEY + "&redirect_uri=" + redirect_uri + DAUM_CLIENT_CALLBACK + "&response_type=code";
		return NAVER_AUTH_HOST + "/oauth2.0/authorize?client_id=" + NAVER_CLIENT_KEY + "&response_type=code&redirect_uri=" + redirectUri + NAVER_CLIENT_CALLBACK +"&state=" + state;
	}
	
	public Map<String, Object> getToken(String code, String state) {
		// 1. url 정보 
		String requestUrl = NAVER_AUTH_HOST + "/oauth2.0/token";
		
		// 2. 헤더 정보
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		// 3. 바디 정보
		Map<String, String> params = new HashMap<String, String>();
		params.put("grant_type", "authorization_code");
		params.put("client_id", NAVER_CLIENT_KEY);
		params.put("client_secret", NAVER_CLIENT_SECRET);
		params.put("code", code);
		params.put("state", state);
		
		// 4. accessType 결과
		return tokenJsonConvertByMap(getResponseBody(requestUrl, headers, params));
	}
	
	public Map<String, Object> getUserInfo(String accessToken) {
		// 1. url 정보 
		String url = NAVER_API_HOST + "/nidlogin/nid/getUserProfile.json";
		
		// 2. 헤더 정보
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "Bearer " + accessToken);
		
		// 3. 바디 정보
		Map<String, String> params = new HashMap<String, String>();
		params.put("response_type", "json");
		
		try {
			String result = getResponseBody(url, null, params);
			logger.info("result : " + result.toString());
			
			Map<String, Object> data = new HashMap<String, Object>();
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject)jsonParser.parse(result.toString());
			JSONObject propertiesJsonObject = (JSONObject) jsonObject.get("properties");
			if(propertiesJsonObject == null) {
				throw new Error("[Naver] User info api error.[error_code: " + (String)jsonObject.get("error_code") + ", message: " + (String)jsonObject.get("message"));
			}
			Map<String, Object> userData = new HashMap<String, Object>();
			userData.put("userid", (String)propertiesJsonObject.get("nickname")); 
			userData.put("id", (long)propertiesJsonObject.get("thumbnail_image"));
			userData.put("nickname", (String)propertiesJsonObject.get("profile_image"));
			data.put("id", (long)jsonObject.get("id"));
			return data;
		} catch (ParseException e) {
			throw new Error(e.getMessage());
		}
	}
	
	public Boolean isSuccess(Map<String, Object> resultUserInfoData) {
		if(resultUserInfoData == null) {
			return false;
		}
		
		if(resultUserInfoData.get("code") == null || resultUserInfoData.get("code") == "") {
			return false;
		}
		
		if((long)resultUserInfoData.get("code") != 200) {
			return false;
		} else {
			return true;
		}
	}
}
