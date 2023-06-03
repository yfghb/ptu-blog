package com.my.blog.controller.content;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.Article;
import com.my.blog.domain.entity.ArticleTag;
import com.my.blog.domain.vo.ArticleDetailVo;
import com.my.blog.domain.vo.PageVo;
import com.my.blog.domain.vo.RecordsVo;
import com.my.blog.service.IArticleService;
import com.my.blog.service.IArticleTagService;
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
 * @since 2023/5/30 19:00
 */
@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Resource
    private IArticleService iArticleService;

    @Resource
    private IArticleTagService iArticleTagService;

    @Resource
    private ITransactionService iTransactionService;

    /**
     * 查询博客文章列表
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @param title 文章标题
     * @param summary 文章摘要
     * @return ResponseResult
     */
    @GetMapping("/list")
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
     * 以id查询文章
     * @param id 文章id
     * @return ResponseResult
     */
    @GetMapping("/{id}")
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
     * 修改文章
     * @param articleDetailVo articleDetailVo
     * @return ResponseResult
     */
    @PutMapping
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
     * 删除文章
     * @param id 文章id
     * @return ResponseResult
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@permissionServiceImpl.hasPermission('content:article:list')")
    public ResponseResult deleteArticleById(@PathVariable @NonNull Long id){
        return ResponseResult.okResult(iTransactionService.deleteArticleAndArticleTag(id));
    }

    /**
     * 新增文章
     * @param vo ArticleDetailVo
     * @return ResponseResult
     */
    @PostMapping
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
