package com.my.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.Article;

/**
 * <p>
 * 文章表 服务类
 * </p>
 *
 * @author WH
 * @since 2023-05-16
 */
public interface IArticleService extends IService<Article> {
    /**
     * 更新文章阅读人数
     * @param id 文章id
     * @return string
     */
    ResponseResult updateViewCount(Long id);

    /**
     * 以id更新文章阅读量
     * @param count 文章阅读量
     * @param id 文章id
     * @return Boolean
     */
    Boolean updateViewCount(Long id,Long count);
}
