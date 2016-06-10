package test.mvc.spring.module.social;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import test.mvc.spring.common.handler.SessionHandler;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class SocialNetworkServiceTwitter extends AbstractSocialNetworkService {
	private static final Logger logger = LoggerFactory.getLogger(SocialNetworkServiceTwitter.class);
	
	private static final String TWITTER_CONSUMER_KEY = "hGQnmNMukGkXwQPNsqvQpg";
	private static final String TWITTER_CONSUMER_SECRET = "2Tb8UKxSNU89hQOa8JSDU2zhEUzHLEsUgJItZ2CrF0";
	
	private Twitter twitter;
	private RequestToken requestToken = null;
	private AccessToken accessToken = null;
	
	public SocialNetworkServiceTwitter() {
		twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
		try {
			requestToken = twitter.getOAuthRequestToken();
		} catch (TwitterException e) {
			throw new Error(e.getMessage());
		}
	}
	
	public Twitter getInstance() {
		return twitter;
	}
	
	public String createOAuthAuthorizationURL(HttpServletRequest request, String redirectUri, String state) {
		SessionHandler.setObjectInfo(request, SessionHandler.STATE, requestToken);
		return requestToken.getAuthorizationURL();
	}
	
	public Map<String, Object> getToken(String code, String state) {
//		// 1. url 정보 
//		String requestUrl = DAUM_HOST + "/oauth2/token";
//		
//		// 2. 헤더 정보
//		Map<String, String> headers = new HashMap<String, String>();
//		headers.put("content-type", "application/x-www-form-urlencoded;charset=utf-8");
//		
//		// 3. 바디 정보
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("client_id", DAUM_CLIENT_KEY);
//		params.put("client_secret", DAUM_CLIENT_SECRET);
//		params.put("redirect_uri", DAUM_CLIENT_CALLBACK);
//		params.put("code", code);
//		params.put("grant_type", "authorization_code");
//		
//		// 4. accessType 결과
//		return tokenJsonConvertByMap(getResponseBody(requestUrl, headers, params));
		return null;
	}
	
	public Map<String, Object> getUserInfo(String accessToken, HttpServletRequest request) {
//		// 1. url 정보
//		String url = DAUM_HOST + "/user/v1/show.json";
//		
//		// 2. 바디 정보
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("access_token", accessToken);
//		params.put("output", "json");
//		
//		try {
//			// 3. json 형태의 결과값
//			String result = getResponseBody(url, params);
//			logger.debug(result);
//			
//			// 4. parser 객체 생성
//			JSONParser jsonParser = new JSONParser();
//			
//			// 5. string 형태의 json 값 parsing
//			JSONObject jsonObject = (JSONObject)jsonParser.parse(result.toString());
//			
//			// 6. 코드값 확인. 200이 아니면 에러 처리
//			long code = (long)jsonObject.get("code");
//			if(!HttpStatus.valueOf((int) code).is2xxSuccessful()) {
//				throw new Error("[Daum] User info api error.[error_code: " + code + ", message: " + (String)jsonObject.get("message"));
//			}
//			
//			// 7. 결과 정보 파싱
//			JSONObject resultJsonObject = (JSONObject) jsonObject.get("result");
//			
//			// 8. 사용자 정보 map에 저장
//			Map<String, Object> userData = new HashMap<String, Object>();
//			userData.put("userid", (String)resultJsonObject.get("userid")); 
//			userData.put("id", (long)resultJsonObject.get("id"));
//			userData.put("nickname", (String)resultJsonObject.get("nickname"));
//			userData.put("imagepath", (String)resultJsonObject.get("imagepath"));
//			userData.put("bigImagePath", (String)resultJsonObject.get("bigImagePath"));
//			
//			return userData;
//		} catch (ParseException e) {
//			throw new Error(e.getMessage());
//		}
		return null;
	}
}
