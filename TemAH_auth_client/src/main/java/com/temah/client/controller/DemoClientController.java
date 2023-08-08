package com.temah.client.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;

@RestController
public class DemoClientController {

    Logger logger = LoggerFactory.getLogger(DemoClientController.class);

    /**
     * client的入口
     */
    @GetMapping("/hello")
    public String hello() {
        return "hello form demo";
    }

    @GetMapping("/user")
    public Principal user(Principal principal) {
        return principal;
    }

    /**
     * 这个接口在从授权服务器拿到授权码后使用URL中附带的授权码请求令牌
     * 这个接口的地址需要配置为 redirect_uri
     */
    @GetMapping("/getToken")
    public String getToken(@RequestParam("code") String code,
                           @RequestParam(value = "client", defaultValue = "") String client) {
        logger.info("get code: {}", code);
        RestTemplate restTemplate = new RestTemplate();
        String access_token_url = "http://localhost:7002/oauth/token";

        MultiValueMap<String, Object> hashMap = new LinkedMultiValueMap<>();
        hashMap.add("grant_type","authorization_code");
        hashMap.add("code",code);
        String client_id = (client!=null && !client.isEmpty())?client:"client_1";
        hashMap.add("client_id",client_id);
        hashMap.add("client_secret","123");
        String redirect_uri = "http://localhost:8010/getToken"+((client!=null && !client.isEmpty())?("?client="+client):"");
        hashMap.add("redirect_uri",redirect_uri);

        return restTemplate.postForEntity(access_token_url, hashMap, String.class).getBody();
    }
}
