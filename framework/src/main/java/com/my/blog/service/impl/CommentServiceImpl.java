package com.my.blog.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.dao.CommentMapper;
import com.my.blog.dao.UserMapper;
import com.my.blog.domain.entity.Comment;
import com.my.blog.domain.entity.User;
import com.my.blog.domain.vo.CommentListVo;
import com.my.blog.service.ICommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author WH
 * @since 2023-05-16
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public JSONObject getComment(Integer pageNum, Integer pageSize, Long articleId,int type) {
        Page<Comment> page = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<Comment> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Comment::getArticleId,articleId)
                .eq(Comment::getRootId,-1)
                .eq(Comment::getType,type)
                .orderByAsc(Comment::getCreateTime);
        Page<Comment> commentPage = commentMapper.selectPage(page, lqw);
        List<Comment> records = commentPage.getRecords();
        List<CommentListVo> voList = new ArrayList<>();
        records.forEach(comment -> {
            CommentListVo vo = new CommentListVo();
            dfs(vo,comment);
            voList.add(vo);
        });
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rows", voList);
        jsonObject.put("total",commentPage.getTotal());
        jsonObject.put("length",commentPage.getSize());
        return jsonObject;
    }


    private void dfs(CommentListVo vo,Comment comment){
        List<CommentListVo> commentList = new ArrayList<>();
        BeanUtils.copyProperties(comment,vo);
        User user = userMapper.selectById(comment.getCreateBy());
        if(user!=null){
            vo.setUsername(user.getUserName());
        }
        LambdaQueryWrapper<Comment> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Comment::getToCommentId,comment.getId());

        List<Comment> comments = commentMapper.selectList(lqw);
        for(Comment c:comments){
            CommentListVo commentListVo = new CommentListVo();
            dfs(commentListVo,c);
            commentList.add(commentListVo);
        }
        vo.setChildren(commentList);
    }


}
