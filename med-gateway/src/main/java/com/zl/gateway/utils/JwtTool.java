package com.zl.gateway.utils;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.zl.gateway.po.UserInfo;
import com.zl.utils.UnauthorizedException;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTool {
    private final JWTSigner jwtSigner;

    public JwtTool(KeyPair keyPair) {
        this.jwtSigner = JWTSignerUtil.createSigner("rs256", keyPair);
    }

    /**
     * 创建 access-token
     *
     * @param userId 用户id
     * @param role 用户角色
     * @param ttl 过期时间
     * @return access-token
     */
    public String createToken(Long userId, String role, Duration ttl) {
        return JWT.create()
                .setPayload("userId", userId)
                .setPayload("role", role)
                .setExpiresAt(new Date(System.currentTimeMillis() + ttl.toMillis()))
                .setSigner(jwtSigner)
                .sign();
    }

    /**
     * 解析token，返回用户信息
     *
     * @param token token
     * @return Map，包含userId和role
     */
    public UserInfo parseToken(String token) {
        if (token == null) {
            throw new UnauthorizedException("未登录");
        }
        JWT jwt;
        try {
            jwt = JWT.of(token).setSigner(jwtSigner);
        } catch (Exception e) {
            throw new UnauthorizedException("无效的token", e);
        }
        if (!jwt.verify()) {
            throw new UnauthorizedException("无效的token");
        }
        try {
            JWTValidator.of(jwt).validateDate();
        } catch (ValidateException e) {
            throw new UnauthorizedException("token已经过期");
        }

        Object userIdPayload = jwt.getPayload("userId");
        Object rolePayload = jwt.getPayload("role");
        if (userIdPayload == null || rolePayload == null) {
            throw new UnauthorizedException("无效的token");
        }

        try {
            UserInfo result = new UserInfo();
            result.setUserId(Long.valueOf(userIdPayload.toString()));
            result.setRole(rolePayload.toString());
            return result;
        } catch (RuntimeException e) {
            throw new UnauthorizedException("无效的token");
        }
    }
}
