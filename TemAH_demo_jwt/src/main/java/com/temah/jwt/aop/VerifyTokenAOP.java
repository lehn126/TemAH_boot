package com.temah.jwt.aop;


import com.temah.jwt.jwt.JWTUtil;
import com.temah.common.constant.MsgConstant;
import com.temah.jwt.exception.AuthException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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

    @Pointcut("@annotation(com.temah.jwt.aop.VerifyToken)")
    public void annotationPointCut() {
    }

    /**
     * 检查request header中的 Authorization 属性存储的jwt token是否有效
     */
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
                throw new AuthException(MsgConstant.TOKEN_MISS_ERROR, "Can't not found token in the request");
            }

            // [ChangeMe] use jwt token validation here for template
            if (!jwtUtil.check(token)) {
                throw new AuthException(MsgConstant.TOKEN_VALID_ERROR, "Token validation failed");
            }
        }
        return joinPoint.proceed();
    }
}
