package com.my.blog.controller;


import com.my.blog.annotation.SystemLog;
import com.my.blog.constant.CommentType;
import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.Comment;
import com.my.blog.enums.AppHttpCodeEnum;
import com.my.blog.service.ICommentService;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author Yang Fan
 * @since 2023/05/16 19:50
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private ICommentService iCommentService;

    /**
     * 查询友链页面的评论列表
     * @param pageNum 当前页
     * @param pageSize 显示条数
     * @param articleId 文章id
     * @return ResponseResult
     */
    @SystemLog(businessName = "获取友联页面的评论列表接口")
    @GetMapping("/linkCommentList")
    public ResponseResult linkCommentList(@NonNull Integer pageNum,
                                          @NonNull Integer pageSize,
                                          @NonNull Long articleId){

        return ResponseResult.okResult(iCommentService.getComment(pageNum,pageSize,articleId, CommentType.LINK));
    }

    /**
     * 查询文章评论列表
     * @param pageNum 当前页
     * @param pageSize 显示条数
     * @param articleId 文章id
     * @return ResponseResult
     */
    @SystemLog(businessName = "获取文章页面的评论列表接口")
    @GetMapping("/commentList")
    public ResponseResult commentList(@NonNull Integer pageNum,
                                      @NonNull Integer pageSize,
                                      @NonNull Long articleId){
        return ResponseResult.okResult(iCommentService.getComment(pageNum,pageSize,articleId,CommentType.ARTICLE));
    }

    /**
     * 新增评论
     * @param comment 评论实体
     * @return ResponseResult
     */
    @PostMapping
    public ResponseResult saveComment(@RequestBody Comment comment){
        comment.setCreateTime(LocalDateTime.now());
        boolean b = iCommentService.save(comment);
        if(!b){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"新增异常");
        }
        return ResponseResult.okResult("新增成功");
    }
}
