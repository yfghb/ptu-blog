package com.my.blog.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Yang Fan
 * @since 2023/5/19 16:43
 */
@Data
public class PageVo {
    private List<RecordsVo> rows;
    private Long total;
    private Long length;
}

