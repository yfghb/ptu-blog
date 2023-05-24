package com.my.blog.controller;

import com.my.blog.domain.ResponseResult;
import com.my.blog.domain.entity.Link;
import com.my.blog.domain.vo.LinkVo;
import com.my.blog.service.ILinkService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 友链 前端控制器
 * </p>
 *
 * @author WH
 * @since 2023-05-16
 */
@RestController
@RequestMapping("/link")
public class LinkController {
    @Resource
    private ILinkService iLinkService;

    @GetMapping("/getAllLink")
    public ResponseResult getAllLink(){
        List<Link> list = iLinkService.list();
        List<LinkVo> voList = new ArrayList<>();
        list.forEach( o -> {
            LinkVo vo = new LinkVo();
            BeanUtils.copyProperties(o,vo);
            voList.add(vo);
        });
        return ResponseResult.okResult(voList);
    }
}
