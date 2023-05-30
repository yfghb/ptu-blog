package com.my.blog.job;

import com.my.blog.annotation.SystemLog;
import com.my.blog.domain.entity.Article;
import com.my.blog.service.IArticleService;
import com.my.blog.utils.RedisCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Yang Fan
 * @since 2023/5/30 15:18
 */
@Component
public class UpdateViewCountJob {

    @Resource
    private RedisCache redisCache;

    @Resource
    private IArticleService articleService;

    @SystemLog(businessName = "定时任务：更新文章阅读量到数据库")
    @Scheduled(cron = "0 0/1 * * * ?")
    public void updateViewCountJob(){
        //获取redis中的浏览量
        Map<String, Integer> viewCountMap = redisCache.getCacheMap("viewCount");
        viewCountMap.forEach((k,v)->{
            Long id = Long.valueOf(k);
            Long viewCount = Long.valueOf(v);
            // 更新到数据库
            articleService.updateViewCount(id,viewCount);
        });

    }
}