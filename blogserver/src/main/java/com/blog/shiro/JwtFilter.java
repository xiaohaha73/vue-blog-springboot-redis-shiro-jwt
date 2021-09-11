package com.blog.shiro;

import cn.hutool.json.JSONUtil;
import com.blog.utils.ResData;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

// 所有请求都会经过的过滤器

@Component
public class JwtFilter extends AuthenticatingFilter {

    @Autowired
    private JwtUtils jwtUtils;

    // 生成token的方法 登陆时候使用得到
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        // 获取传递过来的请求头，如果是不要登录就能访问的接口返回null 后面在控制器上使用注解@RequiresAuthentication进行判断
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");
        if (StringUtils.isEmpty(jwt)) {
            return null;
        }

        return new JwtToken(jwt); // 生成token
    }

    // 身份校验 对所有请求的请求头中的Authorization进行判断
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");
        // 调用JWT工具类进行分析token是否有效
        if (StringUtils.isEmpty(jwt)) {
            // 不需要携带token和没有token的情况 放行
            return true;
        } else {

            // 进行校验
            Claims claims = jwtUtils.getClaimByToken(jwt); // 根据token解密获取信息
            // 过期或者为空都不能放行
            if (claims == null || jwtUtils.isTokenExpired(claims.getExpiration())) {
                throw new ExpiredCredentialsException("token失效请重新登录");
            }

            // 放行执行登录操作 成功 | 失败
            return executeLogin(servletRequest,servletResponse);

        }
    }

    // 重写执行executeLogin失败的方法 让他给前端抛出错误的信息
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // 获取异常信息
        Throwable throwable = e.getCause() == null ? e:e.getCause();
        // 调用自己封装好的返回固定格式数据的工具类
        ResData resData = ResData.err(throwable.getMessage());
        String jsonData = JSONUtil.toJsonStr(resData); // 需要转换成json字符串

        try {
            PrintWriter writer = httpServletResponse.getWriter();
            writer.print(jsonData);
            writer.flush();
            writer.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return false;
    }


    // 配置跨域
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }

        return super.preHandle(request, response);
    }
}
