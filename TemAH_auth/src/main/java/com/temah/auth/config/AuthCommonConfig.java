package com.temah.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class AuthCommonConfig {

    @Value("${security.oauth2.jwt.signingKey:mySigningKey}")
    private String jwtSigningKey = "mySigningKey";

    /**
     * 密码明文加密方式配置
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 令牌存储
     * InMemoryTokenStore:token存储在本机的内存之中。
     * JdbcTokenStore:token存储在数据库之中。
     * JwtTokenStore:token不会存储到任何介质中。
     * RedisTokenStore:token存储在Redis数据库之中。
     */
    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
        //return new JwtTokenStore(accessTokenConverter());
    }

    /**
     * 修改Token格式为JWT
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        // 签名秘钥
        jwtAccessTokenConverter.setSigningKey(jwtSigningKey);
        return jwtAccessTokenConverter;
    }
}
