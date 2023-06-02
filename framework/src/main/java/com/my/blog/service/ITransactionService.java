package com.my.blog.service;

import com.my.blog.domain.entity.*;
import com.my.blog.domain.vo.SysRoleVo;

import java.util.List;

/**
 * @author Yang Fan
 * @since 2023/6/1 15:59
 */
public interface ITransactionService {
    /**
     * 文章表和文章标签中间表事务更新
     * @param article article实体
     * @param list ArticleTag列表
     * @return Boolean
     */
    Boolean updateArticleAndArticleTag(Article article, List<ArticleTag> list);

    /**
     * 文章表和文章标签中间表事务保存
     * @param article article实体
     * @param list ArticleTag列表
     * @return Boolean
     */
    Boolean saveArticleAndArticleTag(Article article, List<ArticleTag> list);

    /**
     * 事务删除tag以及articleTag
     * @param id tagId
     * @return Boolean
     */
    Boolean deleteTagAndArticleTag(Long id);

    /**
     * 事务删除article以及articleTag
     * @param id articleId
     * @return Boolean
     */
    Boolean deleteArticleAndArticleTag(Long id);

    /**
     * 事务保存用户及角色
     * @param user 用户实体
     * @param list 角色列表
     * @return Boolean
     */
    Boolean saveUserAndRoles(User user, List<SysUserRole> list);

    /**
     * 事务保存角色以及角色菜单记录
     * @param sysRole SysRole
     * @param list List<SysRoleMenu>
     * @return Boolean
     */
    Boolean saveRoleAndRoleMenu(SysRole sysRole,List<SysRoleMenu> list);

    /**
     * 事务删除用户和权限记录
     * @param id 用户id
     */
    void deleteUserAndUserRoles(Long id);

    /**
     * 事务删除角色以及用户角色记录
     * @param id 角色id
     */
    void deleteRoleAndUserRoles(Long id);

    /**
     * 事务更新角色以及角色菜单记录
     * @param sysRole SysRole
     * @param list List<SysRoleMenu>
     * @return Boolean
     */
    Boolean updateRoleAndRoleMenu(SysRole sysRole,List<SysRoleMenu> list);
}
