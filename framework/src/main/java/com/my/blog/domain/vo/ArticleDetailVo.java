package com.my.blog.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Yang Fan
 * @since 2023/5/19 16:55
 */
@Data
public class ArticleDetailVo {
    private Long categoryId;
    private String categoryName;
    private String content;
    private String thumbnail;
    private String summary;
    private LocalDateTime createTime;
    private Long id;
    private String isComment;
    private String title;
    private Long viewCount;
    private String isTop;
    private List<Long> tags;

}
