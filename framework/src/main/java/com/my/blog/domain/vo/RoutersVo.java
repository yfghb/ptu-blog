package com.my.blog.domain.vo;

import com.my.blog.domain.entity.SysMenu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Yang Fan
 * @since 2023/5/27 16:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoutersVo {
    private List<SysMenu> menus;
}
