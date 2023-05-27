package com.my.blog.service;

import java.util.List;

/**
 * @author Yang Fan
 * @since 2023/5/27 16:26
 */
public interface IRoleService {
    /**
     * 查询权限id列表
     * @param id 用户id
     * @return List<String>
     */
    List<String> selectRoleKeyByUserId(Long id);
}
