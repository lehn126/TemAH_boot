package com.temah.monitor.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.UUID;

@EnableWebSecurity
public class WebSecurityConfig {

    private final AdminServerProperties adminServer;

    private final SecurityProperties security;

    public WebSecurityConfig(AdminServerProperties adminServer, SecurityProperties security) {
        this.adminServer = adminServer;
        this.security = security;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(this.adminServer.path("/"));

        http
                .authorizeRequests(authorizeRequests -> {
                    authorizeRequests
                            // 对所有静态资源和登录页面进行放行处理
                            .antMatchers("/assets/**").permitAll()
                            .antMatchers("/actuator/info").permitAll()
                            .antMatchers("/actuator/health").permitAll()
                            .antMatchers("/login").permitAll()
                            // 其它所有请求都必须经过身份认证
                            .anyRequest().authenticated();
                })
                // 配置登录和注销逻辑
                .formLogin(formLogin -> {
                    formLogin.loginPage("/login").successHandler(successHandler);
                })
                .logout(logout -> {
                    logout.logoutUrl("/logout");
                })
                // 开启HTTP-Basic认证, 这是Spring Boot Admin客户端所需的
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> {
                    // 启用Cookie的CSRF保护
                    csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                            .ignoringRequestMatchers(
                                    // 禁用Spring Boot Admin客户端对于注册（注销）请求的CSRF保护
                                    new AntPathRequestMatcher("/instances",
                                            HttpMethod.POST.toString()),
                                    new AntPathRequestMatcher("/instances/*",
                                            HttpMethod.DELETE.toString()),
                                    // 禁用Spring Boot Admin客户端对于actuator接口的CSRF保护
                                    new AntPathRequestMatcher("/actuator/**")
                            );
                })
                .rememberMe(rememberMe -> {
                    // rememberMe的token有效期配置为2周
                    rememberMe.key(UUID.randomUUID().toString()).tokenValiditySeconds(1209600);
                });
        return http.build();
    }
}
