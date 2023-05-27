package com.my.blog.controller;

import com.my.blog.domain.LoginUser;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.SysMenu;
import com.my.blog.domain.entity.User;
import com.my.blog.domain.vo.AdminUserInfoVo;
import com.my.blog.domain.vo.RoutersVo;
import com.my.blog.domain.vo.UserInfoVo;
import com.my.blog.enums.AppHttpCodeEnum;
import com.my.blog.exception.SystemException;
import com.my.blog.service.IAdminLoginService;
import com.my.blog.service.IRoleService;
import com.my.blog.service.ISysMenuService;
import com.my.blog.utils.BeanCopyUtils;
import com.my.blog.utils.SecurityUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Yang Fan
 * @since 2023/5/27 15:14
 */
@RestController
public class AdminLoginController {
    @Resource
    private IAdminLoginService iAdminLoginService;

    @Resource
    private IRoleService roleService;

    @Resource
    private ISysMenuService iSysMenuService;


    /**
     * 后台用户登录接口
     * @param user 用户实体
     * @return ResponseResult
     */
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
//提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return iAdminLoginService.login(user);
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return iAdminLoginService.logout();
    }


    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
//获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
//根据用户id查询权限信息
        List<String> perms =
                iSysMenuService.selectPermsByUserId(loginUser.getUser().getId());
//根据用户id查询角色信息
        List<String> roleKeyList =
                roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
//获取用户信息
        User user = loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
//封装数据返回
        AdminUserInfoVo adminUserInfoVo = new
                AdminUserInfoVo(perms,roleKeyList,userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }

    @GetMapping("/getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        Long userId = SecurityUtils.getUserId();
//查询menu 结果是tree的形式
        List<SysMenu> menus = iSysMenuService.selectRouterMenuTreeByUserId(userId);
//封装数据返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }



}
