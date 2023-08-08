package com.temah.client.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests( authorizeRequests -> {
                    authorizeRequests
                            .antMatchers("/getToken/**", "/login", "/error").permitAll()
                            .anyRequest().authenticated();
                })
                .oauth2Login();
    }
}
