package com.my.blog.controller.content;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.blog.constant.DelFlag;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.Category;
import com.my.blog.domain.vo.CategoryVo;
import com.my.blog.service.ICategoryService;
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
@RequestMapping("/content/category")
public class CategoryController {

    @Resource
    private ICategoryService iCategoryService;

    /**
     * 查询所有的分类
     * @return ResponseResult
     */
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Category::getDelFlag, DelFlag.DEL_FALSE);
        List<Category> list = iCategoryService.list(lqw);
        List<CategoryVo> voList = new ArrayList<>();
        list.forEach(tag -> {
            CategoryVo vo = new CategoryVo();
            vo.setId(tag.getId());
            vo.setName(tag.getName());
            voList.add(vo);
        });
        return ResponseResult.okResult(voList);
    }


    /**
     * 分类表分页查询
     * @param pageNum 当前页
     * @param pageSize 一页显示的记录数
     * @param name 分类名称
     * @param status 状态
     * @return ResponseResult
     */
    @GetMapping("/list")
    @PreAuthorize("@permissionServiceImpl.hasPermission('content:category:list')")
    public ResponseResult getCategoryPage(@NonNull Integer pageNum,
                                          @NonNull Integer pageSize,
                                          String name,
                                          Integer status){
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.like(name!=null && !name.isEmpty(),Category::getName,name);
        lqw.eq(status!=null && (status == 0 || status == 1),Category::getStatus,status);
        Page<Category> page = iCategoryService.page(new Page<>(pageNum, pageSize), lqw);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows",page.getRecords());
        jsonObject.put("total",page.getTotal());
        jsonObject.put("length",page.getSize());
        return ResponseResult.okResult(jsonObject);
    }

    /**
     * 以id查询类别
     * @param id 分类id
     * @return ResponseResult
     */
    @GetMapping("/{id}")
    @PreAuthorize("@permissionServiceImpl.hasPermission('content:category:list')")
    public ResponseResult getCategoryById(@PathVariable @NonNull Long id){
        return ResponseResult.okResult(iCategoryService.getById(id));
    }

    /**
     * 修改分类
     * @param category category实体
     * @return ResponseResult
     */
    @PutMapping
    @PreAuthorize("@permissionServiceImpl.hasPermission('content:category:list')")
    public ResponseResult updateCategory(@RequestBody @NonNull Category category){
        return ResponseResult.okResult(iCategoryService.updateById(category));
    }




    /**
     * 以id删除类别
     * @param idStr 分类id字符串
     * @return ResponseResult
     */
    @DeleteMapping("/{idStr}")
    @PreAuthorize("@permissionServiceImpl.hasPermission('content:category:list')")
    public ResponseResult deleteCategoryById(@PathVariable @NonNull String idStr){
        String[] ids = idStr.split(",");
        for(String id:ids){
            Long categoryId = Long.valueOf(id);
            iCategoryService.removeById(categoryId);
        }
        return ResponseResult.okResult();
    }

    /**
     * 新增分类
     * @param category 分类实体
     * @return ResponseResult
     */
    @PostMapping
    @PreAuthorize("@permissionServiceImpl.hasPermission('content:category:list')")
    public ResponseResult saveCategory(@RequestBody Category category){
        return ResponseResult.okResult(iCategoryService.save(category));
    }

    /**
     * 导出分类
     * @return ResponseResult
     */
    @GetMapping("/export")
    @PreAuthorize("@permissionServiceImpl.hasPermission('content:category:export')")
    public ResponseResult export(){
        return ResponseResult.okResult(iCategoryService.list());
    }

}
