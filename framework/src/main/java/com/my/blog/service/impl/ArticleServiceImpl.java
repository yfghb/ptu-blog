package com.my.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.dao.ArticleMapper;
import com.my.blog.domain.entity.Article;
import com.my.blog.service.IArticleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * <p>
 * 文章表 服务实现类
 * </p>
 *
 * @author WH
 * @since 2023-05-16
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Override
    public String updateViewCount(Long id) {
        return articleMapper.updateViewCount(id)?"success":"updateViewCount fail!";
    }
}
