package com.agilent.csda.component;

import com.agilent.csda.common.ConfigConstant;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Custom UsernamePasswordAuthenticationFilter
 */
public class CustomUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private String usernameParameter = "username";
    private String passwordParameter = "password";
    private boolean postOnly = true;

    public CustomUsernamePasswordAuthenticationFilter() {
        /**
         * specify intercept POST request to /login
         */
        super(new AntPathRequestMatcher(ConfigConstant.LOGIN_FORM_SUBMIT_URL, "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            /*
             * Get the user name and password information input by the user from the http request
             * Modify it here to to receive the parameters in the form of json
             */
            String username = this.obtainUsername(request);
            String password = this.obtainPassword(request);
            if (StringUtils.isEmpty(username) && StringUtils.isEmpty(password)) {
                throw new UsernameNotFoundException("CustomUsernamePasswordAuthenticationFilter获取用户认证信息失败");
            }
            /*
             * Create an unauthenticated user authentication token using the username and password
             */
            CustomUsernamePasswordAuthenticationToken authRequest = new CustomUsernamePasswordAuthenticationToken(username, password);

            this.setDetails(request, authRequest);
            /**
             * User authentication by calling the corresponding AuthenticationProvider through the AuthenticationManager
             */
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }

    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(this.usernameParameter);
    }

    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(this.passwordParameter);
    }

    protected void setDetails(HttpServletRequest request, CustomUsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    public void setUsernameParameter(String usernameParameter) {
        Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
        this.usernameParameter = usernameParameter;
    }

    public void setPasswordParameter(String passwordParameter) {
        Assert.hasText(passwordParameter, "Password parameter must not be empty or null");
        this.passwordParameter = passwordParameter;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getUsernameParameter() {
        return this.usernameParameter;
    }

    public final String getPasswordParameter() {
        return this.passwordParameter;
    }
}
