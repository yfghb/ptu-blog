package com.my.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.blog.domain.entity.Article;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 文章表 Mapper 接口
 * </p>
 *
 * @author WH
 * @since 2023-05-16
 */
public interface ArticleMapper extends BaseMapper<Article> {
    /**
     * 更新阅读人数
     * @param id 文章id
     * @return Boolean
     */
    @Update("update article set view_count = view_count + 1 where id = #{id}")
    Boolean updateViewCount(Long id);
}
