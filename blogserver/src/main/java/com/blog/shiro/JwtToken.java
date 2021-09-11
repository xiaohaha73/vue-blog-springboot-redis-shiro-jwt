package com.blog.shiro;


import org.apache.shiro.authc.AuthenticationToken;

/**
 * 生成jwt Token的类
 */
public class JwtToken implements AuthenticationToken {

    private final String token; // 生成的token

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
