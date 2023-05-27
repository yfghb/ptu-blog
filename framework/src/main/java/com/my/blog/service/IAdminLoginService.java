package com.my.blog.service;

import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.User;

/**
 * @author Yang Fan
 * @since 2023/5/27 15:15
 */
public interface IAdminLoginService {
    /**
     * 系统后台登录接口
     * @param user 用户实体
     * @return ResponseResult
     */
    ResponseResult login(User user);

    /**
     * 系统后台登出接口
     * @return ResponseResult
     */
    ResponseResult logout();
}
