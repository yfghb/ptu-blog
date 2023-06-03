package com.my.blog.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.SysMenu;
import com.my.blog.enums.AppHttpCodeEnum;
import com.my.blog.service.ISysMenuService;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Yang Fan
 * @since 2023/6/1 19:49
 */
@RestController
@RequestMapping("/system/menu")
public class MenuController {


    @Resource
    private ISysMenuService iSysMenuService;

    /**
     * 查询菜单列表
     * @param menuName 菜单名
     * @param status 状态
     * @return ResponseResult
     */
    @GetMapping("/list")
    @PreAuthorize("@permissionServiceImpl.hasPermission('system:menu:list')")
    public ResponseResult getMenuList(String menuName, Integer status){
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
    @PostMapping
    @PreAuthorize("@permissionServiceImpl.hasPermission('system:menu:add')")
    public ResponseResult addMenu(@RequestBody @NonNull SysMenu sysMenu){
        return ResponseResult.okResult(iSysMenuService.save(sysMenu));
    }

    /**
     * 以id查询菜单
     * @param id 菜单id
     * @return ResponseResult
     */
    @GetMapping("/{id}")
    @PreAuthorize("@permissionServiceImpl.hasPermission('system:menu:query')")
    public ResponseResult getMenuById(@PathVariable @NonNull Long id){
        return ResponseResult.okResult(iSysMenuService.getById(id));
    }

    /**
     * 更新菜单
     * @param sysMenu SysMenu
     * @return ResponseResult
     */
    @PutMapping
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
    @DeleteMapping("/{id}")
    @PreAuthorize("@permissionServiceImpl.hasPermission('system:menu:remove')")
    public ResponseResult deleteMenuById(@PathVariable @NonNull Long id){
        return ResponseResult.okResult(iSysMenuService.removeById(id));
    }

    /**
     * 以id获取树形菜单列表
     * @return ResponseResult
     */
    @GetMapping("/roleMenuTreeselect/{id}")
    @PreAuthorize("@permissionServiceImpl.hasPermission('system:role:edit')")
    public ResponseResult treeSelect(@PathVariable @NonNull Long id){
        return ResponseResult.okResult(iSysMenuService.getTreeMenuByUserId(id));
    }


    /**
     * 获取树形权限列表
     * @return ResponseResult
     */
    @GetMapping("/treeselect")
    @PreAuthorize("@permissionServiceImpl.hasPermission('system:role:add')")
    public ResponseResult treeSelect(){
        return ResponseResult.okResult(iSysMenuService.getTreeMenuByUserId(1L).getMenus());
    }

}
