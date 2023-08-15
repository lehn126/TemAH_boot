package com.temah.common.http;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;

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

    private static HttpHeaders putRequestHeaders(MediaType contentType, Map<String, String> headers) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(contentType);
        if (headers != null && !headers.isEmpty()) {
            Set<Map.Entry<String, String>> entries = headers.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                requestHeaders.add(entry.getKey(), entry.getValue());
            }
        }
        return requestHeaders;
    }

    public static HttpEntity<Object> buildStringBodyPostEntity(MediaType contentType,
                                                               Map<String, String> headers,
                                                               String body,
                                                               String bearerToken) {
        HttpHeaders httpHeaders = putRequestHeaders(contentType, headers);
        if (bearerToken != null && !bearerToken.isEmpty()) {
            httpHeaders.setBearerAuth(bearerToken);
        }
        return new HttpEntity<>(body, httpHeaders);
    }

    public static HttpEntity<Object> buildFormUrlEncodedPostEntity(Map<String, String> headers,
                                                                   Map<String, String> stringEntities,
                                                                   String bearerToken) {
        HttpHeaders httpHeaders = putRequestHeaders(MediaType.APPLICATION_FORM_URLENCODED, headers);
        if (bearerToken != null && !bearerToken.isEmpty()) {
            httpHeaders.setBearerAuth(bearerToken);
        }

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        if (stringEntities != null) {
            stringEntities.forEach(map::add);
        }
        return new HttpEntity<>(map, httpHeaders);
    }

    public static HttpEntity<Object> buildMultiFormDataPostEntity(Map<String, String> headers,
                                                                  Map<String, String> txtEntities,
                                                                  Map<String, String> jsonEntities,
                                                                  Map<String, List<File>> fileEntities,
                                                                  String bearerToken) {
        HttpHeaders httpHeaders = putRequestHeaders(MediaType.MULTIPART_FORM_DATA, headers);
        if (bearerToken != null && !bearerToken.isEmpty()) {
            httpHeaders.setBearerAuth(bearerToken);
        }

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map = putTextEntities(map, txtEntities);
        map = putJsonEntities(map, jsonEntities);
        map = putFileEntities(map, fileEntities);

        return new HttpEntity<>(map, httpHeaders);
    }

    private static MultiValueMap<String, Object> putTextEntities(MultiValueMap<String, Object> map,
                                                                 Map<String, String> txtEntities) {
        if (map == null) {
            map = new LinkedMultiValueMap<>();
        }
        if (txtEntities != null) {
            HttpHeaders h = new HttpHeaders();
            h.setContentType(MediaType.TEXT_PLAIN);
            for (Map.Entry<String, String> entry : txtEntities.entrySet()) {
                HttpEntity<Object> httpEntity = new HttpEntity<>(entry.getValue(), h);
                map.add(entry.getKey(), httpEntity);
            }
        }
        return map;
    }

    private static MultiValueMap<String, Object> putJsonEntities(MultiValueMap<String, Object> map,
                                                                 Map<String, String> jsonEntities) {
        if (map == null) {
            map = new LinkedMultiValueMap<>();
        }
        if (jsonEntities != null) {
            HttpHeaders h = new HttpHeaders();
            h.setContentType(MediaType.APPLICATION_JSON);
            for (Map.Entry<String, String> entry : jsonEntities.entrySet()) {
                HttpEntity<Object> entity = new HttpEntity<>(entry.getValue(), h);
                map.add(entry.getKey(), entity);
            }
        }
        return map;
    }

    private static MultiValueMap<String, Object> putFileEntities(MultiValueMap<String, Object> map,
                                                                 Map<String, List<File>> fileEntities) {
        if (map == null) {
            map = new LinkedMultiValueMap<>();
        }
        if (fileEntities != null) {
            HttpHeaders h = new HttpHeaders();
            h.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            for (Map.Entry<String, List<File>> entry : fileEntities.entrySet()) {
                String name = entry.getKey();
                List<File> files = entry.getValue();
                if (files != null && !files.isEmpty()) {
                    for (File file : files) {
                        Resource resource = new FileSystemResource(file);
                        HttpEntity<Object> entity = new HttpEntity<>(resource, h);
                        map.add(name, entity);
                    }
                }
            }
        }
        return map;
    }

}
