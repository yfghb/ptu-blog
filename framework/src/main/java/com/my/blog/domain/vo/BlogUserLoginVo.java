package com.my.blog.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Yang Fan
 * @since 2023/5/22 11:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogUserLoginVo {
    private String token;
    private UserInfoVo userInfo;
}
