package com.zl.interceptor;

import cn.hutool.core.util.StrUtil;
import com.zl.utils.UserContext;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserInfoInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.获取请求头中的用户信息
        String userId = request.getHeader("user-id");
        // 2.判断是否为空
        if (StrUtil.isNotBlank(userId)) {
            // 不为空，保存到ThreadLocal
            UserContext.setUser(Long.valueOf(userId));
        }
        // 3.放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 移除用户
        UserContext.removeUser();
    }
}