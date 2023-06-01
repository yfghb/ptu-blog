package com.my.blog.controller;

import com.my.blog.annotation.SystemLog;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.User;
import com.my.blog.enums.AppHttpCodeEnum;
import com.my.blog.service.IUserService;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author Yang Fan
 * @since 2023/05/16 19:47
 */
@RestController
public class UserController {
    @Resource
    private IUserService iUserService;

    @Resource
    private PasswordEncoder passwordEncoder;


    /**
     * 用户登录
     * @param user 用户实体
     * @return ResponseResult
     */
    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        return iUserService.login(user);
    }

    /**
     * 用户登出
     * @return ResponseResult
     */
    @PostMapping("/logout")
    public ResponseResult logout(){
        return iUserService.logout();
    }

    /**
     * 查询用户信息接口
     * @param userId 用户id
     * @return ResponseResult
     */
    @SystemLog(businessName = "查询用户信息接口")
    @GetMapping("/user/userInfo")
    public ResponseResult getById(Long userId){
        User user = iUserService.getById(userId);
        if(user==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"不存在的用户");
        }
        return ResponseResult.okResult(user);
    }

    /**
     * 更新用户信息接口
     * @param user 用户实体
     * @return ResponseResult
     */
    @SystemLog(businessName = "更新用户信息接口")
    @PutMapping("/user/userInfo")
    public ResponseResult updateUserInfo(@RequestBody User user){
        boolean b = iUserService.updateById(user);
        if(!b){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"更新异常");
        }
        return ResponseResult.okResult("更新成功");
    }

    /**
     * 新增用户
     * @param user 用户实体
     * @return ResponseResult
     */
    @PostMapping("/user/register")
    public ResponseResult save(@RequestBody @NonNull User user){
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        return ResponseResult.okResult(iUserService.save(user));
    }
}
