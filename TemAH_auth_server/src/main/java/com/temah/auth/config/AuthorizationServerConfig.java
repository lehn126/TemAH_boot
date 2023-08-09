package com.temah.auth.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.temah.auth.jose.Jwks;
import com.temah.auth.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.time.Duration;
import java.util.UUID;

/**
 * 授权服务
 */
@Configuration
public class AuthorizationServerConfig {

    @Value("${security.oauth2.authServer.authCodeValiditySeconds:300}")
    private long authCodeValiditySeconds = 300L;

    @Value("${security.oauth2.authServer.accessTokenValiditySeconds:7200}")
    private long accessTokenValiditySeconds = 7200L;

    @Value("${security.oauth2.authServer.refreshTokenValiditySeconds:259200}")
    private long refreshTokenValiditySeconds = 259200L;

    private PasswordEncoder passwordEncoder;

    /**
     * An instance of UserDetailsService for retrieving users to authenticate.
     */
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUserDetailsService(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * A Spring Security filter chain for the Protocol Endpoints
     * <p>
     * OAuth2AuthorizationEndpointConfigurer provides the ability to customize the OAuth2 Authorization endpoint.
     * It defines extension points that let you customize the pre-processing, main processing, and post-processing
     * logic for OAuth2 authorization requests.
     * <p>
     * AuthenticationConverter: extract authorization information from HttpServletRequest.
     * AuthenticationProvider: authenticating the information get from authorizationRequestConverter.
     * AuthenticationSuccessHandler: used for handling “authenticated” and returning the Authorization response.
     * AuthenticationFailureHandler: used for handling an authorization request exception and returning error response.
     * consentPage: The URI of the custom consent page to redirect resource owners to if consent is required during the authorization request flow.
     */
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .authorizationEndpoint(authorizationEndpoint -> {
//                    authorizationEndpoint
//                            .authorizationRequestConverter()
//                            .authorizationRequestConverters()
//                            .authenticationProvider()
//                            .authenticationProviders()
//                            .authorizationResponseHandler()
//                            .errorResponseHandler()
//                            .consentPage("");
                })
                .oidc(Customizer.withDefaults());	// Enable OpenID Connect 1.0

        http
                // Redirect to the login page when not authenticated from the
                // authorization endpoint
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(
                                new LoginUrlAuthenticationEntryPoint("/login"))
                )
                // Accept access tokens for User Info and/or Client Registration
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

        return http.build();
    }

    /**
     * An instance of RegisteredClientRepository for managing clients.
     * A RegisteredClient is a representation of a client that is registered with the authorization server.
     * A client must be registered with the authorization server before it can initiate an authorization grant flow,
     * such as authorization_code or client_credentials.
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        // The ID (use UUID here) that uniquely identifies the RegisteredClient.
        RegisteredClient client1 = RegisteredClient.withId(UUID.randomUUID().toString())
                // The client identifier.
                .clientId("client_1")
                // A descriptive name used for the client.
                .clientName("client1")
                // The client’s secret. The value should be encoded using Spring Security’s PasswordEncoder.
                // {noop} represents the PasswordEncoder id for Spring Security’s NoOpPasswordEncoder.
                //.clientSecret("{noop}secret")
                .clientSecret(passwordEncoder.encode("123"))
                // The authentication method(s) that the client may use.
                // The supported values are client_secret_basic, client_secret_post,
                // private_key_jwt, client_secret_jwt, and none (public clients).
                //
                // client_secret_basic: ClientSecretBasicAuthenticationConverter:
                //     Format to "Basic {Base64.encode(client_id:client_secret)}" and put into the
                //     request header (Authorization) to request the AccessToken.
                // client_secret_post: ClientSecretPostAuthenticationConverter:
                //     Put client_id and client_secret into request form to request the AccessToken.
                // client_secret_jwt: JwtClientAssertionAuthenticationConverter:
                //     Using client_secret generates jwt through HMAC algorithm
                // private_key_jwt: JwtClientAssertionAuthenticationConverter:
                //     Generate jwt using a private key and expose the public key to the authorization server
                // none: PublicClientAuthenticationConverter:
                //     PKCE (Proof Key for Code Exchange)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                // The supported AuthorizationGrantType values are ('password' mode not supported):
                // authorization_code: OAuth2AuthorizationCodeAuthenticationConverter
                // refresh_token: OAuth2RefreshTokenAuthenticationConverter
                // client_credentials: OAuth2ClientCredentialsAuthenticationConverter
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                // Here we have to add a redirect URI containing path /login/oauth2/code/${registrationClientId} to
                // let OAuth2LoginAuthenticationFilter use granted code to request AccessToken
                .redirectUri("http://127.0.0.1:8010/login/oauth2/code/client1-app")
                .redirectUri("http://127.0.0.1:8010/login/oauth2/code/client1-authorized")
                .redirectUri("http://127.0.0.1:8010/authorized")
                .redirectUri("https://www.baidu.com")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .scope("client1.all")
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .tokenSettings(TokenSettings.builder()
                        .authorizationCodeTimeToLive(Duration.ofSeconds(authCodeValiditySeconds))
                        .accessTokenTimeToLive(Duration.ofSeconds(accessTokenValiditySeconds))
                        .reuseRefreshTokens(true)
                        .refreshTokenTimeToLive(Duration.ofSeconds(refreshTokenValiditySeconds))
                        .build())
                .build();

        RegisteredClient client2 = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("client_2")
                .clientName("client2")
                .clientSecret(passwordEncoder.encode("123"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://127.0.0.1:8010/login/oauth2/code/client2-app")
                .redirectUri("http://127.0.0.1:8010/authorized")
                .scope(OidcScopes.OPENID)
                .scope("client2.all")
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
                .build();

        return new InMemoryRegisteredClientRepository(client1, client2);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = Jwks.generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }
}
