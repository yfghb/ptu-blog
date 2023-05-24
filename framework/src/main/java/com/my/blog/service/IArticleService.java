package com.my.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
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
    String updateViewCount(Long id);
}
