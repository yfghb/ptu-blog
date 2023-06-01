package com.my.blog.service;

import com.my.blog.domain.entity.Article;
import com.my.blog.domain.entity.ArticleTag;

import java.util.List;

/**
 * @author Yang Fan
 * @since 2023/6/1 15:59
 */
public interface ITransactionService {
    /**
     * 文章表和文章标签中间表事务保存
     * @param article article实体
     * @param list ArticleTag列表
     * @return Boolean
     */
    Boolean updateArticleAndArticleTag(Article article, List<ArticleTag> list);
}
