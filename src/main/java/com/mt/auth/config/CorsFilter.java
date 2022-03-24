package com.mt.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * CORS 终极解决方案
 * 注： 如果将服务器放在网关后面，则不要开启本类
 */

@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
public class CorsFilter implements Filter {

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;
    response.setHeader("Access-Control-Allow-Credentials", "true");
    response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,PUT,DELETE,PATCH,HEAD");
    response.setHeader("Access-Control-Allow-Max-Age", "3600");
    response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
    response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));

    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
      response.setStatus(HttpServletResponse.SC_OK);
    } else {
      filterChain.doFilter(servletRequest, servletResponse);
    }
  }

}
