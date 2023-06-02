package com.my.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.blog.constant.DelFlag;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.*;
import com.my.blog.domain.vo.*;
import com.my.blog.service.*;
import org.springframework.beans.BeanUtils;
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
@RequestMapping("/content")
public class ContentController {

    @Resource
    private IArticleService iArticleService;

    @Resource
    private ITagService iTagService;

    @Resource
    private IArticleTagService iArticleTagService;

    @Resource
    private ICategoryService iCategoryService;

    @Resource
    private ITransactionService iTransactionService;

    @Resource
    private ILinkService iLinkService;

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

    /**
     * 查询博客文章列表
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @param title 文章标题
     * @param summary 文章摘要
     * @return ResponseResult
     */
    @GetMapping("/article/list")
    @PreAuthorize("@permissionServiceImpl.hasPermission('content:article:list')")
    public ResponseResult getArticlePage(@NonNull Integer pageNum,
                                         @NonNull Integer pageSize,
                                          String title,
                                          String summary){
        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
        lqw.like(title!=null && !title.isEmpty(),Article::getTitle,title);
        lqw.like(summary!=null && !summary.isEmpty(),Article::getSummary,summary);
        // 分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);
        Page<Article> articlePage = iArticleService.page(page, lqw);
        PageVo pageVo = new PageVo();
        List<RecordsVo> records = new ArrayList<>();
        // 拷贝进vo
        articlePage.getRecords().forEach(obj -> {
            RecordsVo vo = new RecordsVo();
            BeanUtils.copyProperties(obj,vo);
            records.add(vo);
        });
        pageVo.setRows(records);
        pageVo.setTotal(articlePage.getTotal());
        pageVo.setLength(articlePage.getSize());
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 分类表分页查询
     * @param pageNum 当前页
     * @param pageSize 一页显示的记录数
     * @param name 分类名称
     * @param status 状态
     * @return ResponseResult
     */
    @GetMapping("/category/list")
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
     * 标签分页查询
     * @param pageNum 当前页
     * @param pageSize 一页显示的记录数
     * @param name 标签名称
     * @return ResponseResult
     */
    @GetMapping("/tag/list")
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
     * 友联分页查询
     * @param pageNum 当前页
     * @param pageSize 一页显示的记录数
     * @param name 分类名称
     * @param status 状态
     * @return
     */
    @GetMapping("/link/list")
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
     * 以id查询文章
     * @param id 文章id
     * @return ResponseResult
     */
    @GetMapping("/article/{id}")
    @PreAuthorize("@permissionServiceImpl.hasPermission('content:article:list')")
    public ResponseResult getArticleById(@PathVariable @NonNull Long id){
        // 拷贝基本信息
        ArticleDetailVo vo = new ArticleDetailVo();
        Article article = iArticleService.getById(id);
        BeanUtils.copyProperties(article,vo);
        // 查询标签列表
        LambdaQueryWrapper<ArticleTag> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ArticleTag::getArticleId,id);
        List<ArticleTag> list = iArticleTagService.list(lqw);
        List<Long> tags = new ArrayList<>();
        list.forEach(obj->tags.add(obj.getTagId()));
        vo.setTags(tags);
        return ResponseResult.okResult(vo);
    }

    /**
     * 以id查询类别
     * @param id 分类id
     * @return ResponseResult
     */
    @GetMapping("/category/{id}")
    @PreAuthorize("@permissionServiceImpl.hasPermission('content:category:list')")
    public ResponseResult getCategoryById(@PathVariable @NonNull Long id){
        return ResponseResult.okResult(iCategoryService.getById(id));
    }

    /**
     * 以id查询标签
     * @param id 标签id
     * @return ResponseResult
     */
    @GetMapping("/tag/{id}")
    @PreAuthorize("@permissionServiceImpl.hasPermission('content:tag:index')")
    public ResponseResult getTagById(@PathVariable @NonNull Long id){
        return ResponseResult.okResult(iTagService.getById(id));
    }

    /**
     * 修改文章
     * @param articleDetailVo articleDetailVo
     * @return ResponseResult
     */
    @PutMapping("/article")
    @PreAuthorize("@permissionServiceImpl.hasPermission('content:article:list')")
    public ResponseResult updateArticle(@RequestBody ArticleDetailVo articleDetailVo){
        Article article = new Article();
        BeanUtils.copyProperties(articleDetailVo,article);
        List<Long> tags = articleDetailVo.getTags();
        List<ArticleTag> list = new ArrayList<>();
        tags.forEach(tagId->{
            ArticleTag articleTag = new ArticleTag();
            articleTag.setArticleId(article.getId());
            articleTag.setTagId(tagId);
            list.add(articleTag);
        });
        return ResponseResult.okResult(iTransactionService.updateArticleAndArticleTag(article, list));
    }

    /**
     * 修改分类
     * @param category category实体
     * @return ResponseResult
     */
    @PutMapping("/category")
    @PreAuthorize("@permissionServiceImpl.hasPermission('content:category:list')")
    public ResponseResult updateCategory(@RequestBody @NonNull Category category){
        return ResponseResult.okResult(iCategoryService.updateById(category));
    }

    /**
     * 修改标签
     * @param tag 标签实体
     * @return ResponseResult
     */
    @PutMapping("/tag")
    @PreAuthorize("@permissionServiceImpl.hasPermission('content:tag:index')")
    public ResponseResult updateTag(@RequestBody @NonNull Tag tag){
        return ResponseResult.okResult(iTagService.updateById(tag));
    }

    /**
     * 删除文章
     * @param id 文章id
     * @return ResponseResult
     */
    @DeleteMapping("/article/{id}")
    @PreAuthorize("@permissionServiceImpl.hasPermission('content:article:list')")
    public ResponseResult deleteArticleById(@PathVariable @NonNull Long id){
        return ResponseResult.okResult(iTransactionService.deleteArticleAndArticleTag(id));
    }

    /**
     * 以id删除类别
     * @param id 分类id
     * @return ResponseResult
     */
    @DeleteMapping("/category/{id}")
    @PreAuthorize("@permissionServiceImpl.hasPermission('content:category:list')")
    public ResponseResult deleteCategoryById(@PathVariable @NonNull Long id){
        return ResponseResult.okResult(iCategoryService.removeById(id));
    }

    /**
     * 以id删除标签
     * @param id 标签id
     * @return ResponseResult
     */
    @DeleteMapping("/tag/{id}")
    @PreAuthorize("@permissionServiceImpl.hasPermission('content:tag:index')")
    public ResponseResult deleteTagById(@PathVariable @NonNull Long id){
        return ResponseResult.okResult(iTransactionService.deleteTagAndArticleTag(id));
    }

    /**
     * 新增分类
     * @param category 分类实体
     * @return ResponseResult
     */
    @PostMapping("/category")
    @PreAuthorize("@permissionServiceImpl.hasPermission('content:category:list')")
    public ResponseResult saveCategory(@RequestBody Category category){
        return ResponseResult.okResult(iCategoryService.save(category));
    }

    /**
     * 新增标签
     * @param tag 标签实体
     * @return ResponseResult
     */
    @PostMapping("/tag")
    @PreAuthorize("@permissionServiceImpl.hasPermission('content:tag:index')")
    public ResponseResult saveTag(@RequestBody Tag tag){
        return ResponseResult.okResult(iTagService.save(tag));
    }

    /**
     * 新增文章
     * @param vo ArticleDetailVo
     * @return ResponseResult
     */
    @PostMapping("/article")
    @PreAuthorize("@permissionServiceImpl.hasPermission('content:article:writer')")
    public ResponseResult saveArticle(@RequestBody @NonNull ArticleDetailVo vo){
        Article article = new Article();
        // 拷贝属性
        BeanUtils.copyProperties(vo,article);
        article.setId(System.currentTimeMillis());
        List<ArticleTag> list = new ArrayList<>();
        vo.getTags().forEach(tagId->{
            ArticleTag articleTag = new ArticleTag();
            articleTag.setTagId(tagId);
            articleTag.setArticleId(article.getId());
            list.add(articleTag);
        });
        // 保存到数据库
        return ResponseResult.okResult(iTransactionService.saveArticleAndArticleTag(article,list));
    }

}
