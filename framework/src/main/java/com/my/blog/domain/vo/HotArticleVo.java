package com.my.blog.domain.vo;

import lombok.Data;

/**
 * @author Yang Fan
 * @since 2023/5/19 15:21
 */
@Data
public class HotArticleVo {
    private Long id;

    private String title;

    private Long viewCount;
}
