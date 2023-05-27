package com.my.blog.service.impl;

import com.my.blog.domain.LoginUser;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.User;
import com.my.blog.service.IAdminLoginService;
import com.my.blog.utils.JwtUtil;
import com.my.blog.utils.RedisCache;
import com.my.blog.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Yang Fan
 * @since 2023/5/27 15:18
 */
@Service
public class IAdminLoginServiceImpl implements IAdminLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new
                UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate =
                authenticationManager.authenticate(authenticationToken);
//判断是否认证通过
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
//获取userid 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
//把用户信息存入redis
        redisCache.setCacheObject("adminlogin:"+userId,loginUser);
//把token封装 返回
        Map<String,String> map = new HashMap<>(16);
        map.put("token",jwt);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
//获取当前登录的用户id
        Long userId = SecurityUtils.getUserId();
//删除redis中对应的值
        redisCache.deleteObject("adminlogin:"+userId);
        return ResponseResult.okResult();

    }
}
