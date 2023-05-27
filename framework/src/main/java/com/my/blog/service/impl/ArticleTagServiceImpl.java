package com.my.blog.service.impl;

import com.my.blog.dao.ArticleTagMapper;
import com.my.blog.domain.entity.ArticleTag;
import com.my.blog.service.IArticleTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章标签关联表 服务实现类
 * </p>
 *
 * @author Yang Fan
 * @since 2023-05-27
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements IArticleTagService {

}
