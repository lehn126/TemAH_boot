package com.temah.jwt.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {

    public static final String DEFAULT_SIGNING_KEY = "mySigningKey";

    @Value("jwt.signingKey")
    private String signingKey = DEFAULT_SIGNING_KEY;

    public String getSigningKey() {
        return signingKey;
    }

    public void setSigningKey(String signingKey) {
        this.signingKey = signingKey;
    }

    public String generate(JWTPayload payload, Date expiresTime) {
        Date now = new Date();
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");
        return JWT.create()
                .withHeader(header)
                .withClaim("userName", payload.getUserName())
                .withClaim("userId", payload.getUserId())
                .withIssuedAt(now)
                .withNotBefore(now)
                .withExpiresAt(expiresTime)
                .sign(Algorithm.HMAC256(getSigningKey()));
    }

    public boolean check(String token) throws JWTVerificationException {
        boolean rt;
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(getSigningKey())).build();
        DecodedJWT jwt = verifier.verify(token);
        Date expiresTime = jwt.getExpiresAt();
        if (expiresTime == null || expiresTime.after(new Date())) {
            rt = true;
        } else {
            rt = false;
        }
        return rt;
    }
}
