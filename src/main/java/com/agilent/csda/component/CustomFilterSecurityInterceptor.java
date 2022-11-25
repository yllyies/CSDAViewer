package com.agilent.csda.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * Custom SecurityInterceptor
 */
@Component
public class CustomFilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {

    /**
     * Inject custom resource role getter
     */
    @Autowired
    private FilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource;

    /**
     * Inject custom AccessDecisionManager
     */
    @Autowired
    public void setAccessDecisionManager(CustomAccessDecisionManager customAccessDecisionManager) {
        super.setAccessDecisionManager(customAccessDecisionManager);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(request, response, chain);
        InterceptorStatusToken token = super.beforeInvocation(fi);
        try {
            /**
             * execute interceptor chain
             */
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.customFilterInvocationSecurityMetadataSource;
    }
}
