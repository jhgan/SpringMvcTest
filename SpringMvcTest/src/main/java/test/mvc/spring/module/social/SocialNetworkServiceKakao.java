package test.mvc.spring.module.social;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocialNetworkServiceKakao extends AbstractSocialNetworkService {
	private static final Logger logger = LoggerFactory.getLogger(SocialNetworkServiceKakao.class);
	
	private static final String KAKAO_AUTH_HOST = "https://kauth.kakao.com";
	private static final String KAKAO_API_HOST = "https://kapi.kakao.com";
	private static final String KAKAO_CLIENT_ID = "66d8c42a4ca66c04e317cb2669ac0e4d";
	private static final String KAKAO_CLIENT_CALLBACK = "/social/kakao/callback";
	
	public String createOAuthAuthorizationURL(String redirectUri, String state) {
		return KAKAO_AUTH_HOST + "/oauth/authorize?client_id=" + KAKAO_CLIENT_ID + "&response_type=code&redirect_uri=" +redirectUri + KAKAO_CLIENT_CALLBACK +"&state=" + state;
	}

	@Override
	public Map<String, Object> getToken(String code, String state) {
		// 1. url 정보 
		String requestUrl = KAKAO_AUTH_HOST + "/oauth/token";
		
		// 2. 헤더 정보
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		// 3. 바디 정보
		Map<String, String> params = new HashMap<String, String>();
		params.put("grant_type", "authorization_code");
		params.put("client_id", KAKAO_CLIENT_ID);
		params.put("code", code);
		params.put("state", state);
		
		// 4. accessType 결과
		return tokenJsonConvertByMap(getResponseBody(requestUrl, headers, params));
	}
	
	@Override
	public Map<String, Object> getUserInfo(String accessToken) {
		String url = KAKAO_API_HOST + "/v1/user/me";
		
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("content-type", "application/x-www-form-urlencoded;charset=utf-8");
		headers.put("Authorization", "Bearer " + accessToken);
		
		try {
			String result = getResponseBody(url, headers, null);
			logger.info("result : " + result.toString());
			
			Map<String, Object> data = new HashMap<String, Object>();
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject)jsonParser.parse(result.toString());
			JSONObject propertiesJsonObject = (JSONObject) jsonObject.get("properties");
			Map<String, Object> userData = new HashMap<String, Object>();
			userData.put("userid", (String)propertiesJsonObject.get("nickname")); 
			userData.put("id", (long)propertiesJsonObject.get("thumbnail_image"));
			userData.put("nickname", (String)propertiesJsonObject.get("profile_image"));
			data.put("id", (long)jsonObject.get("id"));
			return data;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Boolean isSuccess(Map<String, Object> resultUserInfoData) {
		return null;
	}
}
