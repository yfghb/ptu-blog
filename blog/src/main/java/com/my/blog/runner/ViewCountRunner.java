package com.my.blog.runner;

import com.my.blog.dao.ArticleMapper;
import com.my.blog.domain.entity.Article;
import com.my.blog.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @author Yang Fan
 * @since 2023/5/30 14:47
 */
@Component
@Slf4j
public class ViewCountRunner implements CommandLineRunner {
    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        //查询博客信息 id viewCount
        List<Article> articles = articleMapper.selectList(null);
        HashMap<String, Integer> viewCountMap = new HashMap<>(16);
        for (Article article : articles) {
            viewCountMap.put(article.getId().toString(),article.getViewCount().intValue());
        }
        log.info(viewCountMap.toString());
        //存储到redis中
        redisCache.setCacheMap("viewCount",viewCountMap);
    }
}
