package com.my.blog.service.impl;

import com.my.blog.domain.entity.Article;
import com.my.blog.domain.entity.ArticleTag;
import com.my.blog.service.IArticleService;
import com.my.blog.service.IArticleTagService;
import com.my.blog.service.ITransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Yang Fan
 * @since 2023/6/1 16:02
 */
@Service
public class ITransactionServiceImpl implements ITransactionService {
    @Resource
    IArticleService iArticleService;

    @Resource
    IArticleTagService iArticleTagService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateArticleAndArticleTag(Article article, List<ArticleTag> list) {
        iArticleService.updateById(article);
        iArticleTagService.saveBatch(list);
        return true;
    }
}
