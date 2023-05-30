package com.my.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.blog.domain.entity.Article;
import org.apache.ibatis.annotations.Param;
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
     * 以id更新文章阅读量
     * @param count 文章阅读量
     * @param id 文章id
     * @return Boolean
     */
    @Update("update article set view_count = #{count} where id = #{id}")
    Boolean updateViewCountById(@Param(value = "id") Long id,@Param(value = "count") Long count);
}
