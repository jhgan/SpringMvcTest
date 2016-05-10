package test.mvc.spring.module.social;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocialNetworkServiceDaum extends AbstractSocialNetworkService {
	private static final Logger logger = LoggerFactory.getLogger(SocialNetworkServiceDaum.class);
	
	private static final String DAUM_HOST = "https://apis.daum.net";
	private static final String DAUM_CLIENT_KEY = "5710146924007080108";
	private static final String DAUM_CLIENT_SECRET = "95de935ac3accea52d0295de9eab5fa8";
	private static final String DAUM_CLIENT_CALLBACK = "/social/daum/callback";
	
	public String createOAuthAuthorizationURL(String redirectUri, String state) {
		return DAUM_HOST + "/oauth2/authorize?client_id=" + DAUM_CLIENT_KEY + "&redirect_uri=" + redirectUri + DAUM_CLIENT_CALLBACK + "&response_type=code";
	}
	
	public Map<String, Object> getToken(String code, String state) {
		// 1. url 정보 
		String requestUrl = DAUM_HOST + "/oauth2/token";
		
		// 2. 헤더 정보
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		// 3. 바디 정보
		Map<String, String> params = new HashMap<String, String>();
		params.put("client_id", DAUM_CLIENT_KEY);
		params.put("client_secret", DAUM_CLIENT_SECRET);
		params.put("redirect_uri", DAUM_CLIENT_CALLBACK);
		params.put("code", code);
		params.put("grant_type", "authorization_code");
		
		// 4. accessType 결과
		return tokenJsonConvertByMap(getResponseBody(requestUrl, headers, params));
	}
	
	public Map<String, Object> getUserInfo(String accessToken) {
		String url = DAUM_HOST + "/user/v1/show";
		
		// 3. 바디 정보
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("access_token", accessToken);
		params.put("output", "json");
		
		try {
			String result = getResponseBody(url, params);
			
			Map<String, Object> data = new HashMap<String, Object>();
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject)jsonParser.parse(result.toString());
			JSONObject resultJsonObject = (JSONObject) jsonObject.get("result");
			Map<String, Object> userData = new HashMap<String, Object>();
			userData.put("userid", (String)resultJsonObject.get("userid")); 
			userData.put("id", (long)resultJsonObject.get("id"));
			String word = (String)resultJsonObject.get("nickname");
			userData.put("nickname", word);
			userData.put("imagepath", (String)resultJsonObject.get("imagepath"));
			userData.put("bigImagePath", (String)resultJsonObject.get("bigImagePath"));
			data.put("code", (long)jsonObject.get("code"));
			data.put("message", (String)jsonObject.get("message"));
			data.put("result", userData);
			return data;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
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
	
	public Boolean jsonParse(String result) throws ParseException {
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject;
		jsonObject = (JSONObject)jsonParser.parse(result);
		
		if((String)jsonObject.get("code") != "200") {
			return false;
		}
		
		JSONObject jsonObjectResult = (JSONObject)jsonObject.get("result");
		logger.info(jsonObjectResult.toJSONString());
		
		logger.info("id : " + (String)jsonObjectResult.get("id"));
		logger.info("nickname : " + (String)jsonObjectResult.get("nickname"));
		logger.info("imagePath : " + (String)jsonObjectResult.get("imagePath"));
		
		return true;
	}
}
