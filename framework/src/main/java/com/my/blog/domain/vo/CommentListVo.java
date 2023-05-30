package com.my.blog.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Yang Fan
 * @since 2023/5/23 21:08
 */
@Data
public class CommentListVo {
    private Long articleId;
    private List<CommentListVo> children;
    private String content;
    private Long createBy;
    private Long id;
    private Long rootId;
    private Long toCommentId;
    private Long toCommentUserId;
    private String toCommentUserName;
    private String username;
    private LocalDateTime createTime;
}
