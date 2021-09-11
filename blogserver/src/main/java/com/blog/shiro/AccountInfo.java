package com.blog.shiro;

import lombok.Data;

import java.io.Serializable;

/**
 * 传递给shiro的非私密用户数据(除去密码信息)
 */
@Data
public class AccountInfo implements Serializable {

    private Long id;
    private String username;
    private String avatar;
    private String email;
    private Integer status;
}
