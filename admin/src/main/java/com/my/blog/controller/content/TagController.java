package com.my.blog.controller.content;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.blog.constant.DelFlag;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.Tag;
import com.my.blog.domain.vo.TagVo;
import com.my.blog.service.ITagService;
import com.my.blog.service.ITransactionService;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yang Fan
 * @since 2023/5/30 19:00
 */
@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Resource
    private ITagService iTagService;

    @Resource
    private ITransactionService iTransactionService;

    /**
     * 查询所有的tag
     * @return ResponseResult
     */
    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        LambdaQueryWrapper<Tag> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Tag::getDelFlag, DelFlag.DEL_FALSE);
        List<Tag> list = iTagService.list(lqw);
        List<TagVo> voList = new ArrayList<>();
        list.forEach(tag -> {
            TagVo vo = new TagVo();
            vo.setId(tag.getId());
            vo.setName(tag.getName());
            voList.add(vo);
        });
        return ResponseResult.okResult(voList);
    }

    /**
     * 标签分页查询
     * @param pageNum 当前页
     * @param pageSize 一页显示的记录数
     * @param name 标签名称
     * @return ResponseResult
     */
    @GetMapping("/list")
    @PreAuthorize("@permissionServiceImpl.hasPermission('content:tag:index')")
    public ResponseResult getTagPage(@NonNull Integer pageNum,
                                     @NonNull Integer pageSize,
                                     String name){
        LambdaQueryWrapper<Tag> lqw = new LambdaQueryWrapper<>();
        lqw.like(name!=null && !name.isEmpty(),Tag::getName,name);
        Page<Tag> page = iTagService.page(new Page<>(pageNum, pageSize),lqw);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows",page.getRecords());
        jsonObject.put("total",page.getTotal());
        jsonObject.put("length",page.getSize());
        return ResponseResult.okResult(jsonObject);
    }

    /**
     * 以id查询标签
     * @param id 标签id
     * @return ResponseResult
     */
    @GetMapping("/{id}")
    @PreAuthorize("@permissionServiceImpl.hasPermission('content:tag:index')")
    public ResponseResult getTagById(@PathVariable @NonNull Long id){
        return ResponseResult.okResult(iTagService.getById(id));
    }

    /**
     * 修改标签
     * @param tag 标签实体
     * @return ResponseResult
     */
    @PutMapping
    @PreAuthorize("@permissionServiceImpl.hasPermission('content:tag:index')")
    public ResponseResult updateTag(@RequestBody @NonNull Tag tag){
        return ResponseResult.okResult(iTagService.updateById(tag));
    }

    /**
     * 以id删除标签
     * @param idStr 标签id字符串
     * @return ResponseResult
     */
    @DeleteMapping("/{idStr}")
    @PreAuthorize("@permissionServiceImpl.hasPermission('content:tag:index')")
    public ResponseResult deleteTagById(@PathVariable @NonNull String idStr){
        String[] ids = idStr.split(",");
        for(String id:ids){
            Long tagId = Long.parseLong(id);
            iTransactionService.deleteTagAndArticleTag(tagId);
        }
        return ResponseResult.okResult();
    }

    /**
     * 新增标签
     * @param tag 标签实体
     * @return ResponseResult
     */
    @PostMapping
    @PreAuthorize("@permissionServiceImpl.hasPermission('content:tag:index')")
    public ResponseResult saveTag(@RequestBody Tag tag){
        return ResponseResult.okResult(iTagService.save(tag));
    }



}
