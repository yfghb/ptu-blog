package com.my.blog.controller;

import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.User;
import com.my.blog.enums.AppHttpCodeEnum;
import com.my.blog.service.IUserService;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author WH
 * @since 2023-05-16
 */
@RestController
public class UserController {
    @Resource
    private IUserService iUserService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        return iUserService.login(user);
    }

    @PostMapping("/logout")
    public ResponseResult logout(){
        return iUserService.logout();
    }

    @GetMapping("/user/userInfo")
    public ResponseResult getById(Long userId){
        User user = iUserService.getById(userId);
        if(user==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"不存在的用户");
        }
        return ResponseResult.okResult(user);
    }

    @PutMapping("/user/userInfo")
    public ResponseResult updateUserInfo(@RequestBody User user){
        boolean b = iUserService.updateById(user);
        if(!b){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"更新异常");
        }
        return ResponseResult.okResult("更新成功");
    }

    @PostMapping("/user/register")
    public ResponseResult save(@RequestBody @NonNull User user){
        return ResponseResult.okResult(iUserService.save(user));
    }
}
