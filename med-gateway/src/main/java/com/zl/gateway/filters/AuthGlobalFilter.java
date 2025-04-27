package com.zl.gateway.filters;

import com.zl.gateway.config.AuthProperties;
import com.zl.gateway.po.UserInfo;
import com.zl.gateway.utils.JwtTool;
import com.zl.utils.CollUtils;
import com.zl.utils.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(AuthProperties.class)
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    private final JwtTool jwtTool;
    private final AuthProperties authProperties;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();  // 建议用 .value()

        // 1. 排除不需要鉴权的路径
        if (isExclude(path)) {
            return chain.filter(exchange);
        }

        // 2. 解析 token
        String token = null;
        List<String> headers = request.getHeaders().get("authorization");
        if (!CollUtils.isEmpty(headers)) {
            token = headers.get(0);
        }
        UserInfo userInfo;
        try {
            userInfo = jwtTool.parseToken(token);
        } catch (UnauthorizedException e) {
            ServerHttpResponse resp = exchange.getResponse();
            resp.setRawStatusCode(401);
            return resp.setComplete();
        }

        // 3. 基于动态配置校验角色权限
        if (!checkPermission(path, userInfo.getRole())) {
            ServerHttpResponse resp = exchange.getResponse();
            resp.setRawStatusCode(403);
            return resp.setComplete();
        }

        // 4. 放行并携带用户信息
        ServerWebExchange swe = exchange.mutate()
                .request(builder -> builder
                        .header("user-id", userInfo.getUserId().toString())
                        .header("user-role", userInfo.getRole())
                ).build();

        return chain.filter(swe);
    }

    private boolean isExclude(String path) {
        for (String pattern : authProperties.getExcludePaths()) {
            if (antPathMatcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 动态读取 rolePaths，判断当前角色是否能访问该 path
     */
    private boolean checkPermission(String path, String role) {
        Map<String, List<String>> rolePaths = authProperties.getRolePaths();
        List<String> allow = rolePaths.get(role);
        if (CollUtils.isEmpty(allow)) {
            // 如果没有为该角色配置任何路径，全部拒绝
            return false;
        }
        // 匹配任意一个 pattern 即可放行
        for (String pattern : allow) {
            if (antPathMatcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
