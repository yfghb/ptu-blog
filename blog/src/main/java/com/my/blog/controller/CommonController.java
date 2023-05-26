package com.my.blog.controller;

import com.my.blog.domain.ResponseResult;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Yang Fan
 * @since 2023/5/26 15:52
 */
@RestController
public class CommonController {

    @Value("${blog.filePath}")
    private String filePath;

    @Value("${blog.ptu-img}")
    private String ptuImg;

    /** 获取当前项目所在的目录 */
    private final String property = System.getProperty("user.dir");


    /**
     * 文件上传
     * @param multipartFile multipart文件
     * @return 文件名
     * @throws IOException IOException
     */
    @PostMapping("/upload")
    public ResponseResult uploadFile(@NonNull MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID() + suffix;
        multipartFile.transferTo(new File(property+filePath+filename));
        String url = ptuImg + filename;
        return ResponseResult.okResult(url);
    }
}
