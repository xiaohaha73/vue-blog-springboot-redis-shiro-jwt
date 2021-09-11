package com.blog.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.pojo.MBlog;
import com.blog.pojo.MUser;

import com.blog.service.MBlogService;
import com.blog.service.impl.MBlogServiceImpl;
import com.blog.shiro.AccountInfo;
import com.blog.utils.ResData;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wang
 * @since 2021-09-04
 */
@RestController
@RequestMapping("/blog")
public class MBlogController {

    @Autowired
    private MBlogService blogService;

    /**
     * 获取所有文章的接口 分页功能
     * @param currentPage 当前页码
     * @return ResData
     */
    @RequestMapping("/blogs")
    public ResData blogs (@RequestParam(defaultValue = "1") int currentPage) {

        Page<MBlog> page = new Page<>(currentPage,3);
        // 按照创建时间进行倒叙
        Page<MBlog> blogs = blogService.page(page, new QueryWrapper<MBlog>().orderByDesc("created"));

        return ResData.ok(blogs);
    }

    /***
     * 根据id获取博客详情
     * @param id 博客id
     * @return ResData
     */
    @RequestMapping("/blog/{id}")
    public ResData getBlogById (@PathVariable(name = "id") Long id) {
        Assert.notNull(id,"文章id不能为空");
        MBlog blog = blogService.getById(id);
        Assert.notNull(blog,"该文章不存在");
        return ResData.ok(blog);
    }


    /***
     * 添加新博客文章
     * @param blog 提交的blog对象
     * @return ResData
     */
    @RequiresAuthentication // 需要权限
    @PostMapping("/blog")
    public ResData addBlog (@RequestBody MBlog blog) {
        Assert.notNull(blog,"提交参数不能为空");
        AccountInfo accountInfo = (AccountInfo) SecurityUtils.getSubject().getPrincipal();// shiro提供的工具获取当前登陆用户的信息
        // System.out.println(accountInfo);

        // 设置作者id
         blog.setUserId(accountInfo.getId());
        // 设置文章创建时间
        blog.setCreated(LocalDateTime.now());
        // 设置文章状态为启用状态
        blog.setStatus(0);
        boolean res = blogService.saveOrUpdate(blog);
        Assert.isTrue(res, "添加文章失败"); // 失败的断言
        return ResData.ok("添加文章成功");
    }

    /***
     * 修改博客文章
     * @param blog 提交的blog对象
     * @return ResData
     */
    @RequiresAuthentication // 需要权限
    @PostMapping("/update")
    public ResData editBlog (@RequestBody MBlog blog) {
        Assert.notNull(blog,"提交参数不能为空");
        QueryWrapper<MBlog> wrapper = new QueryWrapper<>();
        wrapper.eq("id",blog.getId());
        boolean res = blogService.update(blog, wrapper);
        Assert.isTrue(res,"更新失败");
        return ResData.ok("更新成功");
    }


}

