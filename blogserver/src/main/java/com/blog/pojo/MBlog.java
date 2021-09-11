package com.blog.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

/**
 * <p>
 * 
 * </p>
 *
 * @author wang
 * @since 2021-09-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MBlog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户id
     */
    @NotEmpty(message = "用户id不能为空")
    private Long userId;

    /**
     * 文章标题
     */
    @NotEmpty(message = "文章标题不能为空")
    private String title;

    /**
     * 文章描述
     */
    @NotEmpty(message = "文章描述不能为空")
    private String description;

    /**
     * 文章内容
     */
    @NotEmpty(message = "文章内容不能为空")
    private String content;

    /**
     * 创建时间
     */
    @NotEmpty(message = "创建时间不能为空")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime created;

    /**
     * 状态
     */
    @NotEmpty(message = "状态值不能为空")
    private Integer status;


}
