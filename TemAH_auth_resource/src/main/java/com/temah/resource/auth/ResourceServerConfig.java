package com.temah.resource.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class ResourceServerConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(sessionManagement -> {
                    // Do not create session, always use token to access resources
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authorizeRequests( authorizeRequests -> {
                    authorizeRequests
                            // [ChangeMe] Update here to add Authorization check config for resources in this service
                            .antMatchers("/**").hasAuthority("SCOPE_client1.all")
                            .anyRequest().authenticated();
                })
                .oauth2ResourceServer()
                .jwt();
        return http.build();
    }

}
