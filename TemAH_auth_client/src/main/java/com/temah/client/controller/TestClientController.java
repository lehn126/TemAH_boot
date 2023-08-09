package com.temah.client.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
public class TestClientController {

    Logger logger = LoggerFactory.getLogger(TestClientController.class);

    @GetMapping("/hello")
    public String hello() {
        return "hello form demo";
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
