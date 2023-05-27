package com.my.blog.service.impl;

import com.my.blog.dao.SysUserRoleMapper;
import com.my.blog.domain.entity.SysUserRole;
import com.my.blog.service.ISysUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户和角色关联表 服务实现类
 * </p>
 *
 * @author Yang Fan
 * @since 2023-05-27
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

}
