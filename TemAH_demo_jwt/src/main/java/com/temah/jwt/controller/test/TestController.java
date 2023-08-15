package com.temah.jwt.controller.test;

import com.temah.common.web.RestResult;
import com.temah.jwt.aop.VerifyToken;
import com.temah.jwt.jwt.JWTPayload;
import com.temah.jwt.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private JWTUtil jwtUtil;

    /**
     * 生成一个jwt token用于测试
     */
    @GetMapping("/token/new")
    public RestResult tokenCreate(HttpServletRequest request) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, 10);
        String token = jwtUtil.generate(new JWTPayload("user-name", "user-id"), c.getTime());
        return RestResult.success(token);
    }

    /**
     * 检查request header中的 Authorization 属性存储的jwt token是否有效
     */
    @GetMapping("/token/verify")
    @VerifyToken
    public RestResult tokenVerify(HttpServletRequest request) {
        return RestResult.success();
    }
}
