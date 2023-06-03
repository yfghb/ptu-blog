package com.my.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.*;
import com.my.blog.domain.vo.SysRoleVo;
import com.my.blog.domain.vo.UserInfoVo;
import com.my.blog.domain.vo.UserStatusVo;
import com.my.blog.enums.AppHttpCodeEnum;
import com.my.blog.service.ISysMenuService;
import com.my.blog.service.ISysRoleService;
import com.my.blog.service.ITransactionService;
import com.my.blog.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yang Fan
 * @since 2023/6/1 19:49
 */
@RestController
@RequestMapping("/system")
public class SystemController {
    @Resource
    private IUserService iUserService;

    @Resource
    private ISysRoleService iSysRoleService;

    @Resource
    private ITransactionService iTransactionService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private ISysMenuService iSysMenuService;

    /**
     * 用户分页查询
     * @param pageNum 当前页
     * @param pageSize 一页显示的记录数
     * @param userName 用户名
     * @param phonenumber 用户电话
     * @param status 用户状态
     * @return ResponseResult
     */
    @GetMapping("/user/list")
    @PreAuthorize("@permissionServiceImpl.hasPermission('system:user:list')")
    public ResponseResult getUserPage(@NonNull Integer pageNum,
                                      @NonNull Integer pageSize,
                                      String userName,
                                      String phonenumber,
                                      Integer status){
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.like(userName!=null && !userName.isEmpty(),User::getUserName,userName)
                .like(phonenumber!=null && !phonenumber.isEmpty(),User::getPhonenumber,phonenumber)
                .eq(status!=null,User::getStatus,status);
        Page<User> page = iUserService.page(new Page<>(pageNum, pageSize), lqw);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows",page.getRecords());
        jsonObject.put("total",page.getTotal());
        jsonObject.put("length",page.getSize());
        return ResponseResult.okResult(jsonObject);
    }

    /**
     * 改变用户状态（禁用用户）
     * @param vo UserStatusVo
     * @return ResponseResult
     */
    @PutMapping("/user/changeStatus")
    @PreAuthorize("@permissionServiceImpl.hasPermission('system:user:edit')")
    public ResponseResult changeStatus(@RequestBody @NonNull UserStatusVo vo){
        if(vo.getUserId()==null || vo.getStatus()==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"参数有null值");
        }
        return ResponseResult.okResult(iUserService.updateUserStatus(vo.getUserId(), vo.getStatus()));
    }

    /**
     * 以id查询用户
     * @param id 用户id
     * @return ResponseResult
     */
    @GetMapping("/user/{id}")
    @PreAuthorize("@permissionServiceImpl.hasPermission('system:user:query')")
    public ResponseResult getUserById(@PathVariable @NonNull Long id){
        return ResponseResult.okResult(iUserService.getById(id));
    }

    /**
     * 查询角色列表
     * @return ResponseResult
     */
    @GetMapping("/role/listAllRole")
    @PreAuthorize("@permissionServiceImpl.hasPermission('system:user:list')")
    public ResponseResult getListAllRole(){
        return ResponseResult.okResult(iSysRoleService.list());
    }

    /**
     * 新增用户并给与权限
     * @param vo UserInfoVo
     * @return ResponseResult
     */
    @PostMapping("/user")
    @PreAuthorize("@permissionServiceImpl.hasPermission('system:user:add')")
    public ResponseResult saveUserAndRoles(@RequestBody UserInfoVo vo){
        User user = new User();
        // 拷贝属性
        BeanUtils.copyProperties(vo,user);
        user.setId(System.currentTimeMillis());
        if(user.getPassword()==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"用户密码不能为空");
        }
        // 加密密码
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        // 创建SysUserRole列表
        List<Long> roleIds = vo.getRoleIds();
        List<SysUserRole> list = new ArrayList<>();
        roleIds.forEach(roleId->{
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(user.getId());
            sysUserRole.setRoleId(roleId);
            list.add(sysUserRole);
        });
        return ResponseResult.okResult(iTransactionService.saveUserAndRoles(user,list));
    }

    /**
     * 删除用户以及权限记录
     * @param idStr 用户id字符串("1,2,3...")
     * @return ResponseResult
     */
    @DeleteMapping("/user/{idStr}")
    @PreAuthorize("@permissionServiceImpl.hasPermission('system:user:remove')")
    public ResponseResult deleteUser(@PathVariable @NonNull String idStr){
        String[] ids = idStr.split(",");
        for(String id:ids){
            Long userId = Long.parseLong(id);
            iTransactionService.deleteUserAndUserRoles(userId);
        }
        return ResponseResult.okResult();
    }

    /**
     * 角色分页查询
     * @param pageNum 当前页
     * @param pageSize 一页显示的记录数
     * @param roleName 角色名
     * @param status 角色状态
     * @return ResponseResult
     */
    @GetMapping("/role/list")
    @PreAuthorize("@permissionServiceImpl.hasPermission('system:role:list')")
    public ResponseResult getRolePage(@NonNull Integer pageNum,
                                      @NonNull Integer pageSize,
                                      String roleName,
                                      Integer status){
        LambdaQueryWrapper<SysRole> lqw = new LambdaQueryWrapper<>();
        lqw.like(roleName!=null && !roleName.isEmpty(),SysRole::getRoleName,roleName)
                .eq(status!=null,SysRole::getStatus,status)
                .orderByAsc(SysRole::getRoleSort);
        Page<SysRole> page = iSysRoleService.page(new Page<>(pageNum, pageSize), lqw);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows",page.getRecords());
        jsonObject.put("total",page.getTotal());
        jsonObject.put("length",page.getSize());
        return ResponseResult.okResult(jsonObject);
    }

    /**
     * 删除角色以及用户角色记录
     * @param idStr 角色id字符串("1,2,3...")
     * @return ResponseResult
     */
    @DeleteMapping("/role/{idStr}")
    @PreAuthorize("@permissionServiceImpl.hasPermission('system:role:remove')")
    public ResponseResult deleteRole(@PathVariable @NonNull String idStr){
        String[] ids = idStr.split(",");
        for(String id:ids){
            Long roleId = Long.parseLong(id);
            iTransactionService.deleteRoleAndUserRoles(roleId);
        }
        return ResponseResult.okResult();
    }

    /**
     * 新增角色
     * @param vo SysRoleVo
     * @return ResponseResult
     */
    @PostMapping("/role")
    @PreAuthorize("@permissionServiceImpl.hasPermission('system:role:add')")
    public ResponseResult saveRole(@RequestBody SysRoleVo vo){
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(vo,sysRole);
        sysRole.setId(System.currentTimeMillis());
        List<SysRoleMenu> list = new ArrayList<>();
        vo.getMenuIds().forEach(menuId->{
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(sysRole.getId());
            sysRoleMenu.setMenuId(menuId);
            list.add(sysRoleMenu);
        });
        return ResponseResult.okResult(iTransactionService.saveRoleAndRoleMenu(sysRole,list));
    }

    /**
     * 以id查询角色
     * @param id 角色id
     * @return ResponseResult
     */
    @GetMapping("/role/{id}")
    @PreAuthorize("@permissionServiceImpl.hasPermission('system:role:query')")
    public ResponseResult getRoleById(@PathVariable @NonNull Long id){
        return ResponseResult.okResult(iSysRoleService.getById(id));
    }

    /**
     * 以id获取树形菜单列表
     * @return ResponseResult
     */
    @GetMapping("/menu/roleMenuTreeselect/{id}")
    @PreAuthorize("@permissionServiceImpl.hasPermission('system:role:edit')")
    public ResponseResult treeSelect(@PathVariable @NonNull Long id){
        return ResponseResult.okResult(iSysMenuService.getTreeMenuByUserId(id));
    }



    /**
     * 获取树形权限列表
     * @return ResponseResult
     */
    @GetMapping("/menu/treeselect")
    @PreAuthorize("@permissionServiceImpl.hasPermission('system:role:add')")
    public ResponseResult treeSelect(){
        return ResponseResult.okResult(iSysMenuService.getTreeMenuByUserId(1L).getMenus());
    }

    /**
     * 更新角色以及角色菜单记录
     * @param vo SysRoleVo
     * @return ResponseResult
     */
    @PutMapping("/role")
    @PreAuthorize("@permissionServiceImpl.hasPermission('system:role:edit')")
    public ResponseResult updateRole(@RequestBody @NonNull SysRoleVo vo){
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(vo,sysRole);
        if(sysRole.getId()==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"角色id不能为空");
        }
        List<SysRoleMenu> list = new ArrayList<>();
        vo.getMenuIds().forEach(menuId->{
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(sysRole.getId());
            sysRoleMenu.setMenuId(menuId);
            list.add(sysRoleMenu);
        });
        return ResponseResult.okResult(iTransactionService.updateRoleAndRoleMenu(sysRole,list));
    }

    /**
     * 查询菜单列表
     * @param menuName 菜单名
     * @param status 状态
     * @return ResponseResult
     */
    @GetMapping("/menu/list")
    @PreAuthorize("@permissionServiceImpl.hasPermission('system:menu:list')")
    public ResponseResult getMenuList(String menuName,Integer status){
        LambdaQueryWrapper<SysMenu> lqw = new LambdaQueryWrapper<>();
        lqw.like(menuName!=null && !menuName.isEmpty(),SysMenu::getMenuName,menuName)
           .eq(status!=null,SysMenu::getStatus,status)
           .orderByAsc(SysMenu::getOrderNum);
        return ResponseResult.okResult(iSysMenuService.list(lqw));
    }

    /**
     * 新增菜单
     * @param sysMenu SysMenu
     * @return ResponseResult
     */
    @PostMapping("/menu")
    @PreAuthorize("@permissionServiceImpl.hasPermission('system:menu:add')")
    public ResponseResult addMenu(@RequestBody @NonNull SysMenu sysMenu){
        return ResponseResult.okResult(iSysMenuService.save(sysMenu));
    }

    /**
     * 以id查询菜单
     * @param id 菜单id
     * @return ResponseResult
     */
    @GetMapping("/menu/{id}")
    @PreAuthorize("@permissionServiceImpl.hasPermission('system:menu:query')")
    public ResponseResult getMenuById(@PathVariable @NonNull Long id){
        return ResponseResult.okResult(iSysMenuService.getById(id));
    }

    /**
     * 更新菜单
     * @param sysMenu SysMenu
     * @return ResponseResult
     */
    @PutMapping("/menu")
    @PreAuthorize("@permissionServiceImpl.hasPermission('system:menu:edit')")
    public ResponseResult updateMenuById(@RequestBody @NonNull SysMenu sysMenu){
        if(sysMenu.getId()==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"菜单id不能为空");
        }
        return ResponseResult.okResult(iSysMenuService.updateById(sysMenu));
    }

    /**
     * 删除菜单
     * @param id 菜单id
     * @return ResponseResult
     */
    @DeleteMapping("/menu/{id}")
    @PreAuthorize("@permissionServiceImpl.hasPermission('system:menu:remove')")
    public ResponseResult deleteMenuById(@PathVariable @NonNull Long id){
        return ResponseResult.okResult(iSysMenuService.removeById(id));
    }
}
