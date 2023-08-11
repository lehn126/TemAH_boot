package com.temah.client.controller;

import com.temah.client.redis.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
public class TestClientController {

    Logger logger = LoggerFactory.getLogger(TestClientController.class);

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${resource.client1.hello:}")
    private String resource1URL;

    @Autowired
    private RedisUtils redisUtils;

    public static final String REDIS_KEY_OAUTH2_ACCESS_TOKEN = "OAUTH2_ACCESS_TOKEN";
    public static final String REDIS_KEY_OAUTH2_ACCESS_TOKEN_EXPIRES = "OAUTH2_ACCESS_TOKEN_EXPIRES";

    private String getAuthToken(OAuth2AuthorizedClient oAuth2AuthorizedClient) {
        String token = (String) redisUtils.get(REDIS_KEY_OAUTH2_ACCESS_TOKEN);
        Long expiresAt = (Long) redisUtils.get(REDIS_KEY_OAUTH2_ACCESS_TOKEN_EXPIRES);

        logger.info("get redis OAuth2 access token {}, expires {}", token, expiresAt);
        if (expiresAt != null) {
            logger.info("redis OAuth2 access token will expires at {}",
                    Instant.ofEpochMilli(expiresAt).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }

        token = oAuth2AuthorizedClient.getAccessToken().getTokenValue();
        if (oAuth2AuthorizedClient.getAccessToken().getExpiresAt() != null) {
            expiresAt = oAuth2AuthorizedClient.getAccessToken().getExpiresAt().toEpochMilli();
        }
        redisUtils.set(REDIS_KEY_OAUTH2_ACCESS_TOKEN, token);
        redisUtils.set(REDIS_KEY_OAUTH2_ACCESS_TOKEN_EXPIRES, expiresAt);

        logger.info("redis set token {}, expires at {}", token, expiresAt);
        if (expiresAt != null) {
            logger.info("redis OAuth2 access token expires time update to {}",
                    Instant.ofEpochMilli(expiresAt).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }

        return token;
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello form demo";
    }

    @GetMapping("/resource1")
    public Object resource1(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient oAuth2AuthorizedClient) {
        // Call restful url from resource server
        HttpHeaders headers = new HttpHeaders();
        // Put Bearer Auth token into request header
        String accessToken = getAuthToken(oAuth2AuthorizedClient);
        headers.setBearerAuth(accessToken);
        Map<String, Object> paramMap = new HashMap<>();
        HttpEntity<Object> httpEntity = new HttpEntity<>(paramMap, headers);
        logger.info("Send request to {}, access token: {}", resource1URL, accessToken);
        return restTemplate.exchange(resource1URL, HttpMethod.GET, httpEntity, String.class);
    }

    @GetMapping("/authorized")
    public Map<String, Object> authorized(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient oAuth2AuthorizedClient) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> map = new HashMap<>(2);
        logger.info("OAuth2AuthorizedClient：{} ", oAuth2AuthorizedClient);
        if (oAuth2AuthorizedClient != null) {
            logger.info("----------------------------------------------------------------------");
            logger.info("PrincipalName: {}", oAuth2AuthorizedClient.getClientRegistration().getClientId());
            logger.info("PrincipalName: {}", oAuth2AuthorizedClient.getPrincipalName());
            logger.info("AccessToken: {}", oAuth2AuthorizedClient.getAccessToken().getTokenValue());
            logger.info("RefreshToke: {}", Objects.requireNonNull(oAuth2AuthorizedClient.getRefreshToken()).getTokenValue());
            logger.info("----------------------------------------------------------------------");
        }
        map.put("authentication", authentication);
        return map;
    }
}
