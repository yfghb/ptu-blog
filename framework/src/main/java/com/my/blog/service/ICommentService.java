package com.my.blog.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.domain.entity.Comment;
import org.springframework.lang.NonNull;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author WH
 * @since 2023-05-16
 */
public interface ICommentService extends IService<Comment> {
    /**
     * 获取评论
     * @param pageNum 当前页
     * @param pageSize 一页显示的条数
     * @param articleId 文章id
     * @return JSON
     */
    JSONObject getComment(Integer pageNum, Integer pageSize, Long articleId,int type);

}
