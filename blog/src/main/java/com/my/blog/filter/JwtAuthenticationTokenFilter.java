package com.my.blog.filter;

import com.alibaba.fastjson.JSON;
import com.my.blog.domain.LoginUser;
import com.my.blog.domain.ResponseResult;
import com.my.blog.enums.AppHttpCodeEnum;
import com.my.blog.utils.JwtUtil;
import com.my.blog.utils.RedisCache;
import com.my.blog.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author Yang Fan
 * @since 2023/5/22 11:16
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Resource
    private RedisCache redisCache;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain) throws ServletException,
            IOException, ServletException {
        //获取请求头中的token
        String token = request.getHeader("token");
        if(!StringUtils.hasText(token)){
            //说明该接口不需要登录 直接放行
            filterChain.doFilter(request, response);
            return;
        }
        //解析获取userid
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            //token超时 token非法
            //响应告诉前端需要重新登录
            ResponseResult result =
                    ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));

            return;
        }
        String userId = claims.getSubject();
        //从redis中获取用户信息
        LoginUser loginUser = redisCache.getCacheObject("bloglogin:" + userId);
        //如果获取不到
        if(Objects.isNull(loginUser)){
            //说明登录过期 提示重新登录
            ResponseResult result =
                    ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
        //存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken = new
                UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
