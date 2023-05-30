package com.my.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.blog.annotation.SystemLog;
import com.my.blog.constant.Status;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.Article;
import com.my.blog.domain.entity.Category;
import com.my.blog.domain.vo.ArticleDetailVo;
import com.my.blog.domain.vo.HotArticleVo;
import com.my.blog.domain.vo.PageVo;
import com.my.blog.domain.vo.RecordsVo;
import com.my.blog.service.IArticleService;
import com.my.blog.service.ICategoryService;
import com.my.blog.utils.RedisCache;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yang Fan
 * @since 2023/05/16 20:54
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private IArticleService iArticleService;

    @Resource
    private ICategoryService iCategoryService;

    @Resource
    private RedisCache redisCache;

    /**
     * 查询热点文章列表
     * @return ResponseResult
     */
    @GetMapping("/hotArticleList")
    public ResponseResult getHotArticleList(){
        List<HotArticleVo> voList = new ArrayList<>();
        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Article::getStatus, Status.ARTICLE_STATUS_NORMAL)
                .orderByDesc(Article::getViewCount);
        Page<Article> page = new Page<>(1,10);
        List<Article> records = iArticleService.page(page).getRecords();
        records.forEach( o -> {
            HotArticleVo vo = new HotArticleVo();
            vo.setId(o.getId());
            vo.setTitle(o.getTitle());
            vo.setViewCount(o.getViewCount());
            voList.add(vo);
        });
        return ResponseResult.okResult(voList);
    }

    /**
     * 查询文章列表
     * @param pageNum 当前页
     * @param pageSize 一页显示的条数
     * @param categoryId 类别id
     * @return ResponseResult
     */
    @GetMapping("/articleList")
    public ResponseResult page(@NonNull Integer pageNum,
                               @NonNull Integer pageSize,
                               @NonNull Long categoryId){
        Page<Article> pageInit = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
        lqw.eq(categoryId>0,Article::getCategoryId,categoryId)
                .eq(Article::getStatus,Status.ARTICLE_STATUS_NORMAL)
                .orderByDesc(Article::getIsTop);
        Page<Article> page = iArticleService.page(pageInit, lqw);
        PageVo pageVo = new PageVo();
        List<RecordsVo> recordsVoList = new ArrayList<>();
        page.getRecords().forEach( o -> {
            RecordsVo recordsVo = new RecordsVo();
            BeanUtils.copyProperties(o,recordsVo);
            String name = iCategoryService.getById(o.getCategoryId()).getName();
            recordsVo.setCategoryName(name);
            recordsVoList.add(recordsVo);
        });
        pageVo.setRows(recordsVoList);
        pageVo.setTotal(page.getTotal());
        pageVo.setLength(page.getSize());
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 以id获取文章
     * @param id 文章id
     * @return ResponseResult
     */
    @GetMapping("/{id}")
    public ResponseResult getArticleDetailById(@PathVariable @NonNull Long id){
        Article article = iArticleService.getById(id);
        // 从redis获取文章阅读量
        Integer viewCount = redisCache.getCacheMapValue("viewCount", article.getId().toString());
        article.setViewCount(viewCount.longValue());
        // 拷贝到vo
        ArticleDetailVo vo = new ArticleDetailVo();
        BeanUtils.copyProperties(article,vo);
        Category category = iCategoryService.getById(article.getCategoryId());
        if(category!=null){
            String name = category.getName();
            vo.setCategoryName(name);
        }
        return ResponseResult.okResult(vo);
    }

    @SystemLog(businessName = "更新文章点击量接口")
    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable @NonNull Long id){
        return ResponseResult.okResult(iArticleService.updateViewCount(id));
    }
}
