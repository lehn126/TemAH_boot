package com.temah.auth.config;

import com.temah.auth.component.JwtTokenEnhancer;
import com.temah.auth.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * 授权服务
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${security.oauth2.jwt.enable:false}")
    private boolean enableJWTToken = false;

    @Value("${security.oauth2.authServer.supportRefreshToken:true}")
    private boolean supportRefreshToken = true;

    @Value("${security.oauth2.authServer.accessTokenValiditySeconds:7200}")
    private int accessTokenValiditySeconds = 7200;

    @Value("${security.oauth2.authServer.refreshTokenValiditySeconds:259200}")
    private int refreshTokenValiditySeconds = 259200;

    private PasswordEncoder passwordEncoder;

    private UserDetailsServiceImpl userDetailsService;

    private AuthenticationManager authenticationManager;

    private ClientDetailsService clientDetailsService;

    private TokenStore tokenStore;

    private JwtAccessTokenConverter jwtAccessTokenConverter;

    private JwtTokenEnhancer jwtTokenEnhancer;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUserDetailsService(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setClientDetailsService(ClientDetailsService clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
    }

    @Autowired
    public void setTokenStore(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Autowired
    public void setJwtTokenEnhancer(JwtTokenEnhancer jwtTokenEnhancer) {
        this.jwtTokenEnhancer = jwtTokenEnhancer;
    }

    @Autowired
    public void setJwtAccessTokenConverter(JwtAccessTokenConverter jwtAccessTokenConverter) {
        this.jwtAccessTokenConverter = jwtAccessTokenConverter;
    }

//    /**
//     * 客户端信息服务
//     */
//    @Bean
//    public UserDetailsService userDetailsService() {
//        // 测试用的纯内存存储用户，被 UserDetailsServiceImpl 覆盖
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        Collection<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("test,add,get,del");
//        manager.createUser(User.withUsername("admin").password(passwordEncoder().encode("admin")).roles("ADMIN").authorities(authorities).build());
//        manager.createUser(User.withUsername("user").password(passwordEncoder().encode("user")).roles("USER").build());
//        return manager;
//    }

//    /**
//     * 认证管理器, 密码模式需要
//     * 使用 userDetailsService 验证用户密码
//     */
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

    /**
     * 授权码服务，授权码的存取
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        // 这里使用内存存储, 可以使用 JdbcAuthorizationCodeServices 将授权码存入数据库
        return new InMemoryAuthorizationCodeServices();
    }

    // 令牌管理服务
    @Bean
    public AuthorizationServerTokenServices authorizationServerTokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        // 认证管理器（默认似乎不需要）
        // services.setAuthenticationManager(authenticationManager);
        // 客户端信息服务
        services.setClientDetailsService(clientDetailsService);
        // 是否产生刷新令牌
        services.setSupportRefreshToken(supportRefreshToken);
        // 令牌存储策略
        services.setTokenStore(tokenStore);
        // 令牌有效期 2小时 单位秒
        services.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
        //刷新令牌有效期3天
        services.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);

        // 配置JWT token的内容增强器，自定义token携带信息
        if (enableJWTToken) {
            TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
            List<TokenEnhancer> delegates = new ArrayList<>();
            delegates.add(jwtTokenEnhancer);
            delegates.add(jwtAccessTokenConverter);
            enhancerChain.setTokenEnhancers(delegates);
            services.setTokenEnhancer(enhancerChain);
        }

        return services;
    }

    /**
     * 用来配置令牌(token)的访问端点和令牌服务(token services)
     * 令牌的存储，用于支持password模式以及令牌刷新
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                // 认证管理器，密码模式需要
                .authenticationManager(authenticationManager)
                // 配置客户端信息服务，authenticationManager 中配了就不需要
                .userDetailsService(userDetailsService)
                // 授权码管理器，授权码模式需要
                .authorizationCodeServices(authorizationCodeServices())
                // 配置令牌服务，不管什么模式都需要 （可以省略？）
                .tokenServices(authorizationServerTokenServices())
                // 配置令牌的存储，authorizationServerTokenServices 中配置了就不需要
                //.tokenStore(tokenStore())
                // 允许使用什么方式来请求令牌 （默认为只能POST）
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);

        // 修改Token格式，默认产生的token是一个 UUID
        if (enableJWTToken) {
            endpoints.accessTokenConverter(jwtAccessTokenConverter);
        }
    }

    /**
     * 配置客户端详情服务
     * <p>
     * 客户端信息可以存到内存中（.inMemory()），也可以通过数据库（.jdbc()）来存储调取详情信息
     * JdbcClientDetailsService
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                // 使用本地内存的方式存储, 为了简易方便测试
                .inMemory()
                // 客户端id
                .withClient("client_1")
                // 该client允许的授权类型（申请令牌的方式）
                .authorizedGrantTypes("authorization_code", "password", "implicit", "client_credentials", "refresh_token")
                // 客户端密钥 (使用 password 模式时请求令牌必须)
                .secret(passwordEncoder.encode("123"))
                // 客户端可以访问的资源列表
                //.resourceIds("res1")
                // 授权范围
                .scopes("all")
                // false 授权码模式时, 申请授权码时是跳转到授权页面, true 不跳转页面, 直接获取到授权码
                .autoApprove(false)
                // 授权码回调地址
                .redirectUris("https://www.baidu.com")
                .and()
                .withClient("client_2")
                // 该client允许的授权类型（申请令牌的方式）
                .authorizedGrantTypes("authorization_code", "password", "implicit", "client_credentials", "refresh_token")
                // 客户端密钥 (使用 password 模式时请求令牌必须)
                .secret(passwordEncoder.encode("123"))
                // 授权范围
                .scopes("all")
                // false 授权码模式时, 申请授权码时是跳转到授权页面, true 不跳转页面, 直接获取到授权码
                .autoApprove(true)
                // 授权码回调地址
                .redirectUris("https://www.baidu.com");
    }

    /**
     * 用来配置令牌端点(Token Endpoint)的安全约束
     * <p>
     * 使用@EnableAuthorizationServer注解后，应用启动后将自动生成下面几个Endpoint：
     * /oauth/authorize：验证
     * /oauth/token：获取token
     * /oauth/confirm_access：用户授权
     * /oauth/error：认证失败
     * /oauth/check_token：资源服务器用来校验token
     * /oauth/token_key：如果使用JWT令牌，则公开用于令牌验证的公钥
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                // /oauth/token_key 获取公钥的endpoint公开。 当使用 JwtToken 且使用非对称加密时，用于获取公钥。
                .tokenKeyAccess("permitAll()")
                // /oauth/check_token 校验token使用的endpoint公开。
                .checkTokenAccess("permitAll()")
                // 允许表单认证（申请令牌）
                .allowFormAuthenticationForClients();
    }
}
