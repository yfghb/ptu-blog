package com.my.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author WH
 * @since 2023-05-16
 */
public interface IUserService extends IService<User> {
    /**
     * 用户登录
     * @param user 用户实体
     * @return ResponseResult
     */
    ResponseResult login(User user);

    /**
     * 用户登出
     * @return ResponseResult
     */
    ResponseResult logout();

    /**
     * 改变用户的可用状态
     * @param id 用户id
     * @param status 状态值
     * @return Boolean
     */
    Boolean updateUserStatus(Long id,Integer status);

}
