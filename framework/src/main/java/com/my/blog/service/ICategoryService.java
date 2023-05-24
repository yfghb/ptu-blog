package com.my.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.blog.domain.entity.Category;

import java.util.List;

/**
 * <p>
 * 分类表 服务类
 * </p>
 *
 * @author WH
 * @since 2023-05-16
 */
public interface ICategoryService extends IService<Category> {
    /**
     * 查询有发布文章里的所有文章类型并去重
     * @return Set CategoryId
     */
    public List<Long> getCategoryIdSet();
}
