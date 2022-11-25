package com.agilent.csda.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.agilent.csda.common.ConfigConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Custom AuthenticationFailureHandler
 */
@Component("customAuthenticationFailureHandler")
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        /*
         * If ConfigConstant.LOGIN_RESPONSE_TYPE="JSON" is configured, JSON is returned, otherwise page jump is used.
         */
        if ("JSON".equalsIgnoreCase(ConfigConstant.LOGIN_RESPONSE_TYPE)) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(exception.getMessage()));
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}
