package com.blog.shiro;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.blog.pojo.MUser;
import com.blog.service.impl.MUserServiceImpl;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * AccountRealm是shiro进行登录或者权限校验的逻辑所在，算是核心了，我们需要重写3个方法，分别是
 *
 * supports：为了让realm支持jwt的凭证校验
 * doGetAuthorizationInfo：权限校验
 * doGetAuthenticationInfo：登录认证校验
 */

@Component
public class AccountRealm extends AuthorizingRealm {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private MUserServiceImpl userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        // 修改成支持jwt
        return token instanceof JwtToken;
    }

    // 登陆权限校验
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 获取到的用户信息
        // Muser user = (Muser) principalCollection.getPrimaryPrincipal();
        // 调用查询用户角色权限的service层查询判断 需要的数据是set<String> permissions 包含这个用户的所有角色
//        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
//        authorizationInfo.setStringPermissions(permissions);
        return null;
    }

    // 登陆信息校验
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        // 获取token值
        JwtToken jwtToken = (JwtToken) authenticationToken;
        // 从token值中解析出用户userid
        String userId = jwtUtils.getClaimByToken((String) jwtToken.getPrincipal()).getSubject();

        // 从数据库中查询数据
        MUser user = userService.getById(userId);

        if (user == null) {
            // 不存在
            throw new UnknownAccountException("账户不存在");
        }
        if (user.getStatus() == -1) {
            // 封号状态
            throw new LockedAccountException("账号已被禁用");
        }

        AccountInfo accountInfo = new AccountInfo();
        // 把查询出的用户信息除密码之外的信息传给shiro
        BeanUtils.copyProperties(user,accountInfo);

        return new SimpleAuthenticationInfo(accountInfo,jwtToken.getCredentials(),getName());
    }
}
