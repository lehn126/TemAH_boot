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
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

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

    /**
     * 针对刷新token时的错误
     * "No AuthenticationProvider found for org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken"
     */
    @Bean
    public PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider() {
        PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider = new PreAuthenticatedAuthenticationProvider();
        preAuthenticatedAuthenticationProvider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper<>(userDetailsService));
        return preAuthenticatedAuthenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                //.authenticationProvider(preAuthenticatedAuthenticationProvider())
                .userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests( authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                // 允许用户采用表单登录的方式进行认证
                .formLogin(formLoginCustomizer -> {})
                // 关闭 CSRF 保护
                .csrf(AbstractHttpConfigurer::disable);
    }
}
