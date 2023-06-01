package com.my.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.blog.domain.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author WH
 * @since 2023-05-16
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 改变用户的可用状态
     * @param id 用户id
     * @param status 状态值
     * @return Boolean
     */
    @Update("update user set status = #{status} where id = #{id}")
    Boolean updateUserStatus(@Param(value = "id") Long id,@Param(value = "status") Integer status);
}
