package com.my.blog.domain.vo;

import lombok.Data;

/**
 * @author Yang Fan
 * @since 2023/5/19 16:49
 */
@Data
public class RecordsVo {
    private String categoryName;
    private String createTime;
    private Long id;
    private String summary;
    private String thumbnail;
    private String title;
    private Long viewCount;
}
