package com.my.blog.service.impl;

import com.my.blog.dao.SysRoleMenuMapper;
import com.my.blog.domain.entity.SysRoleMenu;
import com.my.blog.service.ISysRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色和菜单关联表 服务实现类
 * </p>
 *
 * @author Yang Fan
 * @since 2023-05-27
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {

}
