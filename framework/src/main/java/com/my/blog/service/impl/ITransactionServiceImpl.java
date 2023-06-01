package com.my.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.my.blog.domain.entity.Article;
import com.my.blog.domain.entity.ArticleTag;
import com.my.blog.domain.entity.SysUserRole;
import com.my.blog.domain.entity.User;
import com.my.blog.service.*;
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
    private IArticleService iArticleService;

    @Resource
    private IArticleTagService iArticleTagService;

    @Resource
    private ITagService iTagService;

    @Resource
    private IUserService iUserService;

    @Resource
    private ISysUserRoleService iSysUserRoleService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateArticleAndArticleTag(Article article, List<ArticleTag> list) {
        iArticleService.updateById(article);
        iArticleTagService.saveBatch(list);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveArticleAndArticleTag(Article article, List<ArticleTag> list) {
        iArticleService.save(article);
        iArticleTagService.saveBatch(list);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteTagAndArticleTag(Long id) {
        iTagService.removeById(id);
        iArticleTagService.remove((new LambdaQueryWrapper<ArticleTag>()).eq(ArticleTag::getTagId,id));
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteArticleAndArticleTag(Long id) {
        iArticleService.removeById(id);
        iArticleTagService.remove((new LambdaQueryWrapper<ArticleTag>()).eq(ArticleTag::getArticleId,id));
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveUserAndRoles(User user, List<SysUserRole> list) {
        iUserService.save(user);
        iSysUserRoleService.saveBatch(list);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteUserAndRoles(Long id) {
        iUserService.removeById(id);
        iSysUserRoleService.remove((new LambdaQueryWrapper<SysUserRole>()).eq(SysUserRole::getUserId,id));
        return true;
    }
}
