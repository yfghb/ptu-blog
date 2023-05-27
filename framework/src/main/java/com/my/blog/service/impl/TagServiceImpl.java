package com.my.blog.service.impl;

import com.my.blog.dao.TagMapper;
import com.my.blog.domain.entity.Tag;
import com.my.blog.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 标签 服务实现类
 * </p>
 *
 * @author Yang Fan
 * @since 2023-05-27
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

}
