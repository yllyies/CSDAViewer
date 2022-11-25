package com.agilent.csda.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.agilent.csda.common.ConfigConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Custom AuthenticationSuccessHandler
 */
@Component("customAuthenticationSuccessHandler")
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private ObjectMapper objectMapper;

    public CustomAuthenticationSuccessHandler() {
        /*
         * Specify the URL of the default login success request and whether to always use the default login successful request URL
         */
        this.setDefaultTargetUrl(ConfigConstant.DEFAULT_LOGIN_SUCCESSFUL_REQUEST_URL);
        this.setAlwaysUseDefaultTargetUrl(ConfigConstant.ALWAYS_USE_DEFAULT_LOGIN_SUCCESSFUL_REQUEST_URL);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        /*
         * If ConfigConstant.LOGIN_RESPONSE_TYPE="JSON" is configured, JSON is returned, otherwise page jump is used.
         */
        if ("JSON".equalsIgnoreCase(ConfigConstant.LOGIN_RESPONSE_TYPE)) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(authentication));
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }

}
