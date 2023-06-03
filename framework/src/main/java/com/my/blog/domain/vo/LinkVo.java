package com.my.blog.domain.vo;

import lombok.Data;

/**
 * @author Yang Fan
 * @since 2023/5/21 15:09
 */
@Data
public class LinkVo {
    private Long id;
    private String name;
    private String logo;
    private String description;
    private String address;
    private String status;
}
