package com.my.blog.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.blog.domain.entity.SysMenu;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author Yang Fan
 * @since 2023-05-27
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    /**
     * 查询角色参数
     * @param userId 用户id
     * @return List<String>
     */
    @Select("SELECT\n" +
            "DISTINCT m.perms\n" +
            "FROM\n" +
            "sys_user_role ur\n" +
            "LEFT JOIN sys_role_menu rm ON ur.role_id = rm.role_id\n" +
            "LEFT JOIN sys_menu m ON m.id = rm.menu_id\n" +
            "WHERE\n" +
            "ur.user_id = #{userId} AND\n" +
            "m.menu_type IN ('C','F') AND\n" +
            "m.status = 0 AND\n" +
            "m.del_flag = 0")
    List<String> selectPermsByUserId(Long userId);

    /**
     * 查询路由
     * @return List<SysMenu>
     */
    @Select("SELECT\n" +
            "DISTINCT m.id, m.parent_id, m.menu_name, m.path, m.component, m.visible,\n" +
            "m.status, IFNULL(m.perms,'') AS perms, m.is_frame, m.menu_type, m.icon,\n" +
            "m.order_num, m.create_time\n" +
            "FROM\n" +
            "sys_menu m\n" +
            "WHERE\n" +
            "m.menu_type IN ('C','M') AND\n" +
            "m.status = 0 AND\n" +
            "m.del_flag = 0\n" +
            "ORDER BY\n" +
            "m.parent_id,m.order_num")
    List<SysMenu> selectAllRouterMenu();

    /**
     * 查询路由树
     * @param userId 用户id
     * @return List<SysMenu>
     */
    @Select("SELECT\n" +
            "DISTINCT m.id, m.parent_id, m.menu_name, m.path, m.component, m.visible,\n" +
            "m.status, IFNULL(m.perms,'') AS perms, m.is_frame, m.menu_type, m.icon,\n" +
            "m.order_num, m.create_time\n" +
            "FROM\n" +
            "sys_user_role ur\n" +
            "LEFT JOIN sys_role_menu rm ON ur.role_id = rm.role_id\n" +
            "LEFT JOIN sys_menu m ON m.id = rm.menu_id\n" +
            "WHERE\n" +
            "ur.user_id = #{userId} AND\n" +
            "m.menu_type IN ('C','M') AND\n" +
            "m.status = 0 AND\n" +
            "m.del_flag = 0\n" +
            "ORDER BY\n" +
            "m.parent_id,m.order_num")
    List<SysMenu> selectRouterMenuTreeByUserId(Long userId);

}
