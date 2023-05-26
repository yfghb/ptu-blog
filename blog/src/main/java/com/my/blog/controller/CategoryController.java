package com.my.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.my.blog.constant.Status;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.Category;
import com.my.blog.domain.vo.CategoryVo;
import com.my.blog.service.ICategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yang Fan
 * @since 2023/05/16 19:51
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private ICategoryService iCategoryService;

    /**
     * 查询分类列表
     * @return ResponseResult
     */
    @GetMapping("/getCategoryList")
    public ResponseResult getCategoryList(){
        List<CategoryVo> voList = new ArrayList<>();

        List<Long> idList = iCategoryService.getCategoryIdSet();
        idList.forEach(id -> {
            LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
            lqw.eq(Category::getStatus, Status.ARTICLE_STATUS_NORMAL);
            lqw.eq(Category::getId,id);
            Category category = iCategoryService.getOne(lqw);
            CategoryVo vo = new CategoryVo();
            vo.setId(category.getId());
            vo.setName(category.getName());
            voList.add(vo);
        });

        return ResponseResult.okResult(voList);
    }
}
