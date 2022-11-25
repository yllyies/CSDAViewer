package com.agilent.csda.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * Custom UsernamePasswordAuthenticationConfig
 */
@Component
public class CustomUsernamePasswordAuthenticationConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    CustomUsernamePasswordAuthenticationProvider customUsernamePasswordAuthenticationProvider;

    @Autowired
    CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        CustomUsernamePasswordAuthenticationFilter authenticationFilter = new CustomUsernamePasswordAuthenticationFilter();

        /*
         * specify the AuthenticationManager, otherwise you cannot authenticate
         */
        authenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));

        /*
         * specify RememberMeServices, otherwise the "Remember Me" cannot work
         */
        authenticationFilter.setRememberMeServices(http.getSharedObject(RememberMeServices.class));

        /*
         * specify a custom authentication success and failure processor
         */
        authenticationFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        authenticationFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);

        /*
         * specify customUsernamePasswordAuthenticationProvider before the UsernamePasswordAuthenticationFilter filter
         */
        http.authenticationProvider(customUsernamePasswordAuthenticationProvider)
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
