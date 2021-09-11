package com.blog.service.impl;

import com.blog.pojo.MBlog;
import com.blog.mapper.MBlogMapper;
import com.blog.service.MBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wang
 * @since 2021-09-04
 */
@Service
public class MBlogServiceImpl extends ServiceImpl<MBlogMapper, MBlog> implements MBlogService {

}
