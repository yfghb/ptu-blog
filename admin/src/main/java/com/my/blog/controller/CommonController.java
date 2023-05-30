package com.my.blog.controller;

import com.my.blog.annotation.SystemLog;
import com.my.blog.domain.ResponseResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Yang Fan
 * @since 2023/5/26 15:52
 */
@RestController
public class CommonController {

    @Value("${admin.filePath}")
    private String filePath;

    @Value("${admin.ptu-img}")
    private String ptuImg;

    /** 获取当前项目所在的目录 */
    private final String property = System.getProperty("user.dir");


    /**
     * 文件上传
     * @param img multipart文件
     * @return 文件名
     * @throws IOException IOException
     */
    @SystemLog(businessName = "文件上传接口")
    @PostMapping("/upload")
    public ResponseResult uploadFile(@NonNull MultipartFile img) throws IOException {
        String originalFilename = img.getOriginalFilename();
        String suffix = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID() + suffix;
        img.transferTo(new File(property+filePath+filename));
        String url = ptuImg + filename;
        return ResponseResult.okResult(url);
    }
}
