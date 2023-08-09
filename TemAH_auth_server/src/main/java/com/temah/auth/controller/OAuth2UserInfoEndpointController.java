package com.temah.auth.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2UserInfoEndpointController {

    /**
     * The default URL used to request user information after client receive the AccessToken
     */
    @GetMapping("/userinfo")
    public Authentication oauth2Userinfo() {
        // [ChangeMe]. Enrich this method to return useful user information
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
