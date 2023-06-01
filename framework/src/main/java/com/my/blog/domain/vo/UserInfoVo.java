package com.my.blog.domain.vo;

import com.my.blog.domain.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Yang Fan
 * @since 2023/6/1 20:30
 */

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class UserInfoVo extends User {
    List<Long> roleIds;

}
