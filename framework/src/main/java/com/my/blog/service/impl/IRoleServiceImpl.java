package com.my.blog.service.impl;

import com.my.blog.dao.RoleMapper;
import com.my.blog.service.IRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yang Fan
 * @since 2023/5/27 16:27
 */
@Service
public class IRoleServiceImpl implements IRoleService {

    @Resource
    private RoleMapper roleMapper;

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
//判断是否是管理员 如果是返回集合中只需要有admin
        if(id == 1L){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
//否则查询用户所具有的角色信息
        return roleMapper.selectRoleKeyByUserId(id);
    }

}
