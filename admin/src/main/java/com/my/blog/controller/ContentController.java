package com.my.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.my.blog.constant.DelFlag;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.Category;
import com.my.blog.domain.entity.Tag;
import com.my.blog.domain.vo.CategoryVo;
import com.my.blog.domain.vo.TagVo;
import com.my.blog.service.ICategoryService;
import com.my.blog.service.ITagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yang Fan
 * @since 2023/5/30 19:00
 */
@RestController
@RequestMapping("/content")
public class ContentController {
    @Resource
    private ITagService iTagService;

    @Resource
    private ICategoryService iCategoryService;

    @GetMapping("/tag/listAllTag")
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

    @GetMapping("/category/listAllCategory")
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
}
