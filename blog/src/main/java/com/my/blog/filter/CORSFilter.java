package com.my.blog.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Yang Fan
 * @since 2023/5/19 15:09
 */
@WebFilter(filterName = "CORSFilter", urlPatterns = "/*")
@Component
public class CORSFilter implements Filter {

    @Value("${system.cross-origin}")
    private String crossOrigin;

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Allow-Origin", crossOrigin);
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "36000");
        response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token,authorization,istoken");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        chain.doFilter(request, response);


    }

    @Override
    public void init(FilterConfig arg0) {

    }
}