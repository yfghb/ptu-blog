package com.my.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.my.blog.domain.entity.*;
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

    @Resource
    private ISysRoleService iSysRoleService;

    @Resource
    private ISysRoleMenuService iSysRoleMenuService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateArticleAndArticleTag(Article article, List<ArticleTag> list) {
        // 先删除原来的记录
        iArticleTagService.remove((new LambdaQueryWrapper<ArticleTag>())
                .eq(ArticleTag::getArticleId,article.getId()));
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
    public Boolean saveRoleAndRoleMenu(SysRole sysRole,List<SysRoleMenu> list) {
        iSysRoleService.save(sysRole);
        iSysRoleMenuService.saveBatch(list);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserAndUserRoles(Long id) {
        iUserService.removeById(id);
        iSysUserRoleService.remove((new LambdaQueryWrapper<SysUserRole>()).eq(SysUserRole::getUserId,id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleAndUserRoles(Long id) {
        iSysRoleService.removeById(id);
        iSysUserRoleService.remove((new LambdaQueryWrapper<SysUserRole>()).eq(SysUserRole::getRoleId,id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateRoleAndRoleMenu(SysRole sysRole, List<SysRoleMenu> list) {
        // 把原来菜单权限的删掉
        iSysRoleMenuService.remove((new LambdaQueryWrapper<SysRoleMenu>())
                .eq(SysRoleMenu::getRoleId,sysRole.getId()));
        iSysRoleService.updateById(sysRole);
        iSysRoleMenuService.saveBatch(list);
        return true;
    }
}
