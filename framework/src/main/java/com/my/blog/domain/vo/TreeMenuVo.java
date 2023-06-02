package com.my.blog.domain.vo;

import com.my.blog.domain.entity.SysMenu;
import lombok.Data;

import java.util.List;

/**
 * @author Yang Fan
 * @since 2023/6/2 15:43
 */
@Data
public class TreeMenuVo {
    List<SysMenu> menus;
    List<Long> checkedKeys;
}
