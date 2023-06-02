package com.my.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.my.blog.constant.SystemConstants;
import com.my.blog.dao.SysMenuMapper;
import com.my.blog.domain.entity.SysMenu;
import com.my.blog.domain.entity.SysRoleMenu;
import com.my.blog.domain.entity.SysUserRole;
import com.my.blog.domain.vo.TreeMenuVo;
import com.my.blog.service.ISysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.service.ISysRoleMenuService;
import com.my.blog.service.ISysUserRoleService;
import com.my.blog.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author Yang Fan
 * @since 2023-05-27
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {
    @Resource
    SysMenuMapper menuMapper;

    @Resource
    ISysUserRoleService iSysUserRoleService;

    @Resource
    ISysRoleMenuService iSysRoleMenuService;

    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员，返回所有的权限
        if (id == 1L) {
            LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(SysMenu::getMenuType, SystemConstants.MENU,
                    SystemConstants.BUTTON);
            wrapper.eq(SysMenu::getStatus, SystemConstants.STATUS_NORMAL);
            List<SysMenu> menus = menuMapper.selectList(wrapper);
            List<String> perms = menus.stream()
                    .map(SysMenu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        //否则返回所具有的权限
        return menuMapper.selectPermsByUserId(id);
    }

    @Override
    public List<SysMenu> selectRouterMenuTreeByUserId(Long userId) {
        List<SysMenu> menus = null;
        //判断是否是管理员
        if(SecurityUtils.isAdmin()){
        //如果是，获取所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        }else{
        //否则，获取当前用户所具有的Menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }
        //构建tree
        //先找出第一层的菜单，然后去找他们的子菜单设置到children属性中
        List<SysMenu> menuTree = builderMenuTree(menus,0L);
        return menuTree;
    }

    @Override
    public TreeMenuVo getTreeMenuByUserId(Long roleId) {
        List<Long> checkedKeys = new ArrayList<>();
        if(roleId==1L){
            List<SysMenu> menus = menuMapper.selectList((new LambdaQueryWrapper<SysMenu>()).eq(SysMenu::getStatus,0));
            menus.forEach(menu->checkedKeys.add(menu.getId()));
        }else {
            List<SysRoleMenu> list = iSysRoleMenuService.list((new LambdaQueryWrapper<SysRoleMenu>()).eq(SysRoleMenu::getRoleId,roleId));
            list.forEach(sysRoleMenu->checkedKeys.add(sysRoleMenu.getMenuId()));
        }
        TreeMenuVo vo = new TreeMenuVo();
        vo.setCheckedKeys(checkedKeys);
        vo.setMenus(getTreeMenu());
        return vo;
    }

    private List<SysMenu> getTreeMenu(){
        LambdaQueryWrapper<SysMenu> lqw = new LambdaQueryWrapper<>();
        lqw.eq(SysMenu::getStatus,0)
                .orderByAsc(SysMenu::getOrderNum);
        List<SysMenu> menus = menuMapper.selectList(lqw);
        List<SysMenu> menuTree = builderMenuTree(menus,0L);
        return menuTree;
    }


    private List<SysMenu> builderMenuTree(List<SysMenu> menus, Long parentId) {
        List<SysMenu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }
    private List<SysMenu> getChildren(SysMenu menu, List<SysMenu> menus) {
        List<SysMenu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }


}
