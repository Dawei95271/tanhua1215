package com.tanhua.app.interceptor;

import com.tanhua.commons.utils.JwtUtils;
import com.tanhua.model.domain.User;
import io.jsonwebtoken.Claims;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/17 17:39
 */
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        boolean ret = JwtUtils.verifyToken(token);
        if(! ret){
            // token不正确，直接返回
            response.setStatus(401);
            return false;
        }
        // 放行
        Claims claims = JwtUtils.getClaims(token);
        String mobile = (String) claims.get("mobile");
        Integer userId = (Integer) claims.get("userId");

        User user = new User();
        user.setId(Long.valueOf(userId));
        user.setMobile(mobile);

        // user对象存入ThreadLocal
        UserHolder.set(user);
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.remove();
    }
}
