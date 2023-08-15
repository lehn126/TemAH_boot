package com.temah.common.http;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RestTemplateUtils {

    private static final RestTemplate restTemplate = new RestTemplate();

    private static String putQueryParams(String strUrl, MultiValueMap<String, String> queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(strUrl);
        String reqUrl = strUrl;
        if (queryParams != null && !queryParams.isEmpty()) {
            URI uri = builder.queryParams(queryParams).build().encode(StandardCharsets.ISO_8859_1).toUri();
            reqUrl = uri.toString();
        }
        return reqUrl;
    }

    public static <T> ResponseEntity<T> processGet(String strUrl,
                                                   Map<String, String> uriVariables,
                                                   MultiValueMap<String, String> queryParams,
                                                   Class<T> responseType) {
        String reqUrl = putQueryParams(strUrl, queryParams);
        return restTemplate.getForEntity(reqUrl, responseType,
                uriVariables == null ? Collections.emptyMap() : uriVariables);
    }

    public static <T> ResponseEntity<T> processGetWithBearerToken(String strUrl,
                                                                  Map<String, String> uriVariables,
                                                                  MultiValueMap<String, String> queryParams,
                                                                  String bearerToken,
                                                                  Class<T> responseType) {
        if (bearerToken != null && !bearerToken.isEmpty()) {
            String reqUrl = putQueryParams(strUrl, queryParams);
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(bearerToken);
            Map<String, Object> paramMap = new HashMap<>();
            HttpEntity<Object> httpEntity = new HttpEntity<>(paramMap, headers);

            return restTemplate.exchange(reqUrl, HttpMethod.GET, httpEntity, responseType,
                    uriVariables == null ? Collections.emptyMap() : uriVariables);
        } else {
            return processGet(strUrl, uriVariables, queryParams, responseType);
        }
    }

    /**
     * If parameter 'request' is a Map, then it should be a MultiValueMap instance like LinkedMultiValueMap
     */
    public static <T> ResponseEntity<T> processPost(String strUrl,
                                                    Map<String, String> uriVariables,
                                                    Object request,
                                                    Class<T> responseType) {
        return restTemplate.postForEntity(strUrl, request, responseType,
                uriVariables == null ? Collections.emptyMap() : uriVariables);
    }

    public static <T> ResponseEntity<T> processPostWithBearerToken(String strUrl,
                                                                   Map<String, String> uriVariables,
                                                                   Object request,
                                                                   String bearerToken,
                                                                   Class<T> responseType) {
        if (bearerToken != null && !bearerToken.isEmpty()) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(bearerToken);
            Map<String, Object> paramMap = new HashMap<>();
            HttpEntity<Object> httpEntity = new HttpEntity<>(paramMap, headers);

            return restTemplate.exchange(strUrl, HttpMethod.POST, httpEntity, responseType,
                    uriVariables == null ? Collections.emptyMap() : uriVariables);
        } else {
            return processPost(strUrl, uriVariables, request, responseType);
        }
    }
}
