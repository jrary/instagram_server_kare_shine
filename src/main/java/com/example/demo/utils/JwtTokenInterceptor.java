package com.example.demo.utils;

import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletResponse;

@Component
public class JwtTokenInterceptor implements HandlerInterceptor {
    private final JwtService jwtService;

    public JwtTokenInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    private void exceptionHandler(HttpServletResponse response, BaseResponseStatus status) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        BaseResponse<Object> failed = new BaseResponse<>(status);
        response.setStatus(401);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(mapper.writeValueAsString(failed));
    }
}
