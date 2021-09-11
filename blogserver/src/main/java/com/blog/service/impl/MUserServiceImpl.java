package com.blog.service.impl;

import com.blog.pojo.MUser;
import com.blog.mapper.MUserMapper;
import com.blog.service.MUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wang
 * @since 2021-09-04
 */
@Service
public class MUserServiceImpl extends ServiceImpl<MUserMapper, MUser> implements MUserService {
//    @Autowired
//    private MUserMapper mUserMapper;
//
//
//    // 查询出所有的用户
//    public List<MUser> getUsers() {
//        return mUserMapper.selectList(null);
//    }

}
