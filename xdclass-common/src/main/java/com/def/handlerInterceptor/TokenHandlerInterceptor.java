package com.def.handlerInterceptor;

import com.def.config.JsonData;
import com.def.module.TokenInfo;
import com.def.utils.CommonUtil;
import com.def.utils.TokenUtils;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class TokenHandlerInterceptor implements HandlerInterceptor {

    public static final ThreadLocal<TokenInfo> userInfo = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("token");

        if (StringUtils.isBlank(token)) {
            token = request.getParameter("token");
            if (StringUtils.isBlank(token)) {
                CommonUtil.returnJsonData(response, JsonData.buildError("请先登录"));
                return false;
            }
        }

        Claims claims = TokenUtils.checkJWT(token);
        if (claims == null) {
            CommonUtil.returnJsonData(response, JsonData.buildError("token不合法"));
            return false;
        }

        Long id = Long.parseLong(claims.get("id").toString());
        String email = (String) claims.get("email");
        String name = (String) claims.get("name");

        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setId(id);
        tokenInfo.setEmail(email);
        tokenInfo.setName(name);

        userInfo.set(tokenInfo);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
