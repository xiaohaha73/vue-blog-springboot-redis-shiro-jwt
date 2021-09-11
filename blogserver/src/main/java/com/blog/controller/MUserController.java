package com.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.pojo.MUser;
import com.blog.service.MUserService;
import com.blog.service.impl.MUserServiceImpl;
import com.blog.shiro.JwtUtils;
import com.blog.utils.ResData;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wang
 * @since 2021-09-04
 */
@RestController
@RequestMapping("/user")
public class MUserController {

    @Autowired
    private MUserService userService;
    @Autowired
    private JwtUtils jwtUtils; // jwt工具


    /***
     * 测试接口
     * @return ResData
     */
    // @RequiresAuthentication // 测试shiro拦截
    // @RequiresRoles("admin") 权限判断的注解
    // @RequestMapping("/test")
    @PostMapping("/test")
    public ResData test(@Validated @RequestBody MUser user) {
        // System.out.println(userService.getUsers());
        // return ResData.ok(userService.getUsers());
//        return ResData.ok(userService.list());
        return ResData.ok(user);
    }


    /***
     * 用户登陆接口
     * @param map {username,password}
     * @return ResData
     */
    @PostMapping("/login")
    public ResData login(@RequestBody HashMap<String,Object> map, HttpServletResponse response) {
        String username = (String) map.get("username");
        String password = (String) map.get("password");

        // 使用springboot提供的断言 注意要到异常处理中定义IllegalArgumentException异常
        Assert.notNull(username,"账号不能为空");
        Assert.notNull(password,"密码不能为空");

        // 查询数据
        QueryWrapper<MUser> queryWrapper = new QueryWrapper<>();
        MUser user = userService.getOne(queryWrapper.eq("username",username));
        // 再来个断言
        Assert.notNull(user,"该用户不存在");

        // 比对密码
        if (!password.equals(user.getPassword())) {
            return ResData.err("账号或密码错误");
        }

        // 密码正确生成token 可以返回给前端 也可以设置到header中 这里设置到header中
        String jwtToken = jwtUtils.generateToken(user.getId());
        response.setHeader("Authorization", jwtToken);
        response.setHeader("Access-control-Expose-Headers", "Authorization");

        // 返回用户信息给前端
        HashMap<String,Object> userInfo = new HashMap<>();
        userInfo.put("username",user.getUsername());
        userInfo.put("userId",user.getId());
        userInfo.put("avatar",user.getAvatar());
        userInfo.put("email",user.getEmail());
        return ResData.ok(userInfo);
    }


    @RequestMapping("/logout")
    @RequiresAuthentication // 需要shiro拦截
    public ResData logout () {
        // shiro提供了接口
        SecurityUtils.getSubject().logout();
        return ResData.ok("退出登陆成功");
    }

}

