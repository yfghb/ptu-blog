package com.my.blog.controller.content;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.Link;
import com.my.blog.domain.vo.LinkVo;
import com.my.blog.enums.AppHttpCodeEnum;
import com.my.blog.service.ILinkService;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Yang Fan
 * @since 2023/6/3 11:14
 */
@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Resource
    private ILinkService iLinkService;

    /**
     * 友联分页查询
     * @param pageNum 当前页
     * @param pageSize 一页显示的记录数
     * @param name 分类名称
     * @param status 状态
     * @return
     */
    @GetMapping("/list")
    @PreAuthorize("@permissionServiceImpl.hasPermission('content:link:list')")
    public ResponseResult getLinkPage(@NonNull Integer pageNum,
                                      @NonNull Integer pageSize,
                                      String name,
                                      Integer status){
        LambdaQueryWrapper<Link> lqw = new LambdaQueryWrapper<>();
        lqw.like(name!=null && !name.isEmpty(),Link::getName,name);
        lqw.eq(status!=null,Link::getStatus,status);
        Page<Link> page = iLinkService.page(new Page<>(pageNum, pageSize), lqw);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows",page.getRecords());
        jsonObject.put("total",page.getTotal());
        jsonObject.put("length",page.getSize());
        return ResponseResult.okResult(jsonObject);
    }


    /**
     * 新增友链
     * @param link Link
     * @return ResponseResult
     */
    @PostMapping
    @PreAuthorize("@permissionServiceImpl.hasPermission('content:link:add')")
    public ResponseResult saveLink(@RequestBody @NonNull Link link){
        if(link.getAddress()==null || link.getAddress().isEmpty()){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"address不能为空");
        }
        return ResponseResult.okResult(iLinkService.save(link));
    }

    /**
     * 以id查询友链
     * @param id 友链id
     * @return ResponseResult
     */
    @GetMapping("/{id}")
    @PreAuthorize("@permissionServiceImpl.hasPermission('content:link:query')")
    public ResponseResult getLinkById(@PathVariable @NonNull Long id){
        return ResponseResult.okResult(iLinkService.getById(id));
    }

    /**
     * 修改友链
     * @param link Link
     * @return ResponseResult
     */
    @PutMapping
    @PreAuthorize("@permissionServiceImpl.hasPermission('content:link:edit')")
    public ResponseResult updateLink(@RequestBody @NonNull Link link){
        return ResponseResult.okResult(iLinkService.updateById(link));
    }

    /**
     * 以id删除友链
     * @param id 友链id
     * @return ResponseResult
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@permissionServiceImpl.hasPermission('content:link:remove')")
    public ResponseResult deleteLinkById(@PathVariable @NonNull Long id){
        return ResponseResult.okResult(iLinkService.removeById(id));
    }

    /**
     * 更新友链的状态
     * @param vo LinkVo
     * @return ResponseResult
     */
    @PutMapping("/changeLinkStatus")
    public ResponseResult changeLinkStatus(@RequestBody @NonNull LinkVo vo){
        if(vo.getId()==null || vo.getStatus()==null || vo.getStatus().isEmpty()){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"id和状态不能为空");
        }
        Link link = iLinkService.getById(vo.getId());
        link.setStatus(vo.getStatus());
        return ResponseResult.okResult(iLinkService.updateById(link));
    }


}
