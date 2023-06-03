package com.my.blog.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.SysRole;
import com.my.blog.domain.entity.SysRoleMenu;
import com.my.blog.domain.vo.SysRoleVo;
import com.my.blog.enums.AppHttpCodeEnum;
import com.my.blog.service.ISysRoleService;
import com.my.blog.service.ITransactionService;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yang Fan
 * @since 2023/6/1 19:49
 */
@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Resource
    private ISysRoleService iSysRoleService;


    @Resource
    private ITransactionService iTransactionService;

    /**
     * 角色分页查询
     * @param pageNum 当前页
     * @param pageSize 一页显示的记录数
     * @param roleName 角色名
     * @param status 角色状态
     * @return ResponseResult
     */
    @GetMapping("/list")
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
     * 查询角色列表
     * @return ResponseResult
     */
    @GetMapping("/listAllRole")
    @PreAuthorize("@permissionServiceImpl.hasPermission('system:user:list')")
    public ResponseResult getListAllRole(){
        return ResponseResult.okResult(iSysRoleService.list());
    }

    /**
     * 删除角色以及用户角色记录
     * @param idStr 角色id字符串("1,2,3...")
     * @return ResponseResult
     */
    @DeleteMapping("/{idStr}")
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
    @PostMapping
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
    @GetMapping("/{id}")
    @PreAuthorize("@permissionServiceImpl.hasPermission('system:role:query')")
    public ResponseResult getRoleById(@PathVariable @NonNull Long id){
        return ResponseResult.okResult(iSysRoleService.getById(id));
    }



    /**
     * 更新角色以及角色菜单记录
     * @param vo SysRoleVo
     * @return ResponseResult
     */
    @PutMapping
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


}
