package com.hfahimi.lb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order(1)
public class RequestCounterFilter implements Filter {

    private final StatService stat;

    public RequestCounterFilter(StatService stat) {
        this.stat = stat;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        stat.hit(((HttpServletRequest) request).getRequestURI());
        chain.doFilter(request, response);
    }
}
