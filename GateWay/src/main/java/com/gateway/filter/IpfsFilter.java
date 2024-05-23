package com.gateway.filter;


import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;


/**
 * @author Shawn i
 * @version 1.0
 * @description: TODO
 * @date 2024/4/29 20:11
 */
@Component
public class IpfsFilter extends ZuulFilter {
    // 定义需要拦截的路径集合
    private Set<String> blockedPaths = new HashSet<>();
    @Autowired
    private RestTemplate restTemplate;

    // 构造方法，初始化需要拦截的路径集合
    public IpfsFilter() {
        // 添加需要拦截的路径到集合中
        blockedPaths.add("/guardian/ipfs/");
        blockedPaths.add("/guardian/cipher/");
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        // 获取token
        String token = request.getHeader("token");
        // 获取请求的URL路径
        String requestURI = request.getRequestURI();
        // 检查请求的URL路径是否在被拦截的路径集合中
        for (String blockedPath : blockedPaths) {
            if (requestURI.startsWith(blockedPath)) {
                if(token != null){
                    // 校验token
                    Boolean result = restTemplate.postForObject("http://localhost:8081/user/verifyToken?token={token}",null, Boolean.class,token);
                    if(Boolean.TRUE.equals(result))
                        return null;
                }
                // 如果请求的URL路径以被拦截的路径开头，则拦截该请求
                ctx.setSendZuulResponse(false); // 不转发请求到目标服务
                ctx.setResponseStatusCode(403); // 设置响应状态码为 403 Forbidden
                ctx.setResponseBody("Access to this path is forbidden"); // 设置响应体内容
                return null; // 返回 null 表示过滤器逻辑执行完毕
            }
        }

        return null;
    }
}
