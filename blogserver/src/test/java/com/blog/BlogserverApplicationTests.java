package com.blog;

import com.blog.pojo.MUser;
import com.blog.service.impl.MUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BlogserverApplicationTests {
    @Autowired
    private MUserServiceImpl userService;

    @Test
    void test01() {
        // 测试mybatis-plus连接
        List<MUser> users = userService.list();
        for (MUser user : users) {
            System.out.println(user);
        }
    }

}
