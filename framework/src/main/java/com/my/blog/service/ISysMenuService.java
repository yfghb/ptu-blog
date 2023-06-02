package com.my.blog.service;

import com.my.blog.domain.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.domain.vo.TreeMenuVo;

import java.util.List;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @author Yang Fan
 * @since 2023-05-27
 */
public interface ISysMenuService extends IService<SysMenu> {
    /**
     * 获取角色参数
     * @param id 用户id
     * @return List<String>
     */
    List<String> selectPermsByUserId(Long id);

    /**
     * 查询路由
     * @param userId 用户id
     * @return List<SysMenu>
     */
    List<SysMenu> selectRouterMenuTreeByUserId(Long userId);

    /**
     * 获取树形菜单
     * @param userId
     * @return
     */
    TreeMenuVo getTreeMenuByUserId(Long userId);
}
