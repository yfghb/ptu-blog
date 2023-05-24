package com.my.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.blog.constant.CommentType;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.Comment;
import com.my.blog.domain.entity.Link;
import com.my.blog.enums.AppHttpCodeEnum;
import com.my.blog.service.ICommentService;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 评论表 前端控制器
 * </p>
 *
 * @author WH
 * @since 2023-05-16
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private ICommentService iCommentService;

    @GetMapping("/linkCommentList")
    public ResponseResult linkCommentList(@NonNull Integer pageNum,
                                          @NonNull Integer pageSize,
                                          @NonNull Long articleId){

        return ResponseResult.okResult(iCommentService.getComment(pageNum,pageSize,articleId, CommentType.LINK));
    }

    @GetMapping("/commentList")
    public ResponseResult commentList(@NonNull Integer pageNum,
                                      @NonNull Integer pageSize,
                                      @NonNull Long articleId){
        return ResponseResult.okResult(iCommentService.getComment(pageNum,pageSize,articleId,CommentType.ARTICLE));
    }

    @PostMapping
    public ResponseResult saveComment(@RequestBody Comment comment){
        boolean b = iCommentService.save(comment);
        if(!b){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"新增异常");
        }
        return ResponseResult.okResult("新增成功");
    }
}
