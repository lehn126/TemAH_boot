package com.temah.auth.config;

import com.temah.auth.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    public void setUserDetailsService(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // 这里暂时只能继续使用被废弃的 WebSecurityConfigurerAdapter 因为加上 AuthorizationServer 之后框架会初始化一个
    // 默认的 WebSecurityConfigurerAdapter 实例 ，因此启动时会报出存在重复的 WebSecurityConfigurerAdapter 实例错误
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests( authorizeRequests -> {authorizeRequests.anyRequest().authenticated();})
//                .formLogin(formLoginCustomizer -> {}) // 允许用户采用表单登录的方式进行认证
//                .csrf(AbstractHttpConfigurer::disable);
//        return http.build();
//    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests( authorizeRequests -> {
                    authorizeRequests.anyRequest().authenticated();
                })
                // 允许用户采用表单登录的方式进行认证
                .formLogin(formLoginCustomizer -> {})
                // 关闭 CSRF 保护
                .csrf(AbstractHttpConfigurer::disable);
    }
}
