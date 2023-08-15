package com.temah.ahfm.controller.global;

import com.temah.common.constant.MsgConstant;
import com.temah.common.web.RestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(annotations = {RestController.class, Controller.class})
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handleAllException(Exception e, HandlerMethod handlerMethod,
                                      HttpMethod httpMethod, HttpServletRequest httpServletRequest) {
        logger.error("{} to {} processed by {} error:{}", httpMethod.name(), httpServletRequest.getRequestURI(),
                handlerMethod.getMethod(), e.getMessage());
        return RestResult.fail(MsgConstant.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
