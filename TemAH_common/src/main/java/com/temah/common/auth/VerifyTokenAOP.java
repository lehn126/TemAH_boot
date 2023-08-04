package com.temah.common.auth;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
public class VerifyTokenAOP {

    private JWTUtil jwtUtil;

    public JWTUtil getJwtUtil() {
        return jwtUtil;
    }

    @Autowired
    public void setJwtUtil(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Pointcut("@annotation(com.temah.common.auth.VerifyToken)")
    public void annotationPointCut() {
    }

    @Around("annotationPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) attributes;
        if (sra != null) {
            HttpServletRequest request = sra.getRequest();
            String token = request.getHeader("Authorization");
            if (token == null || token.isEmpty()) {
                token = request.getParameter("Authorization");
            }
            if (token == null || token.isEmpty()) {
                throw new AuthenticationException("Can't not found token in the request");
            }
            if (!jwtUtil.check(token)) {
                throw new AuthenticationException("Token validation failed");
            }
        }
        return joinPoint.proceed();
    }
}
