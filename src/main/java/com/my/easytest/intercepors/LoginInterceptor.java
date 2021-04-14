package com.my.easytest.intercepors;

import com.my.easytest.exception.ServiceException;
import com.my.easytest.common.TokenDb;
import com.my.easytest.constants.UserConstants;
import com.my.easytest.dto.TokenDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @Author G_ALLEN
 * @Date 2021/1/11 21:13
 **/

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenDb tokenDb;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String tokenStr = request.getHeader(UserConstants.LOGIN_TOKEN);

        String requestUri = request.getRequestURI();
        log.info("request.getRequestURI() " + requestUri);

        //如果为swagger文档地址,直接通过
        boolean swaggerFlag = requestUri.contains("swagger")
                //过滤spring默认错误页面
                || requestUri.equals("/error")
                //过滤csrf
                || requestUri.equals("/csrf")
                //过滤favicon.ico
                || requestUri.equals("/favicon.ico")
                //演示map local 不用校验是否登录
                || requestUri.equals("/report/showMapLocal")
                || requestUri.equals("/");
        if(swaggerFlag){
            return true;
        }

        //如果请求中不包含token
        if(StringUtils.isEmpty(tokenStr)){
            response.setStatus(401);
            ServiceException.throwEx("客户端未传token " + requestUri);
        }

        //获取token
        TokenDto tokenDto = tokenDb.getTokenDto(tokenStr);
        //如果user未登录
        if (Objects.isNull(tokenDto)){
            response.setStatus(401);
            ServiceException.throwEx("用户未登录");
            return false;
        }else {
            return true;
        }

    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {

    }

}
