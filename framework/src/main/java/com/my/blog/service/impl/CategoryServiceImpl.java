package com.my.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.dao.CategoryMapper;
import com.my.blog.domain.entity.Category;
import com.my.blog.service.ICategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 分类表 服务实现类
 * </p>
 *
 * @author WH
 * @since 2023-05-16
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public List<Long> getCategoryIdSet() {
        return categoryMapper.getCategoryIdSet();
    }
}
