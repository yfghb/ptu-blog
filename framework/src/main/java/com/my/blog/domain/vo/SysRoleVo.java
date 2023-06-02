package com.my.blog.domain.vo;

import com.my.blog.domain.entity.SysRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Yang Fan
 * @since 2023/6/2 15:43
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysRoleVo extends SysRole {
    public List<Long> menuIds;
}
