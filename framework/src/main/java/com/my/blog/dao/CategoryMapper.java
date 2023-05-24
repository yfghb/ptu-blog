package com.my.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.blog.domain.entity.Category;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 分类表 Mapper 接口
 * </p>
 *
 * @author WH
 * @since 2023-05-16
 */
public interface CategoryMapper extends BaseMapper<Category> {
    /**
     * 查询有发布文章里的所有文章类型并去重
     * @return List<Long>
     */
    @Select("select distinct category_id from article")
    List<Long> getCategoryIdSet();
}
