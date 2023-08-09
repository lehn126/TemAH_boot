package com.temah.client.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class ClientSecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests( authorizeRequests -> {
                    authorizeRequests
                            .antMatchers("/favicon.ico","/error").permitAll()
                            .anyRequest().authenticated();
                })
                // OAuth2Login() will use OAuth2 (or OIDC) to authenticate users,
                // filling Principal with information from JWT or userInfo endpoints.
                // OAuth2Client() does not authenticate users, but will seek permission
                // from the OAuth2 authorization server for the resources (scope) it needs to access.
                // Use Oauth2Client() still need to authenticate the user, such as through formLogin()
                .oauth2Login(Customizer.withDefaults())
                .oauth2Client(Customizer.withDefaults());
        return http.build();
    }
}
