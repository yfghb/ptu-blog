package com.my.blog.dao;

import org.apache.ibatis.annotations.Select;


import java.util.List;

/**
 * @author Yang Fan
 * @since 2023/5/27 16:23
 */
public interface RoleMapper {
    /**
     * 查询权限id列表
     * @param userId 用户id
     * @return List<String>
     */
    @Select("SELECT r.role_key\n" +
            "FROM sys_user_role ur\n" +
            "LEFT JOIN sys_role r ON ur.role_id = r.id\n" +
            "WHERE\n" +
            "ur.user_id = #{userId} AND\n" +
            "r.status = 0 AND r.del_flag = 0")
    List<String> selectRoleKeyByUserId(Long userId);

}
