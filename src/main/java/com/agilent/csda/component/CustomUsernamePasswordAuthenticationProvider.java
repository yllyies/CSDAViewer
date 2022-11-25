package com.agilent.csda.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Custom UsernamePasswordAuthenticationProvider
 */
@Component("customUsernamePasswordAuthenticationProvider")
public class CustomUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsServiceImpl;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        /**
         * Convert unauthenticated Authentication into a custom user authentication token
         */
        CustomUsernamePasswordAuthenticationToken authenticationToken = (CustomUsernamePasswordAuthenticationToken) authentication;

        /**
         * get user information according token. If exist, verify that the user password is correct.
         */
        UserDetails user = userDetailsServiceImpl.loadUserByUsername((String) (authenticationToken.getPrincipal()));
        if (user == null) {
            throw new InternalAuthenticationServiceException("CustomUsernamePasswordAuthenticationProvider获取认证用户信息失败");
        } /*else if(!this.passwordEncoder.matches((CharSequence) authenticationToken.getCredentials(), user.getPassword())) {
            throw new BadCredentialsException("username  or password is not correct");
        }*/

        /**
         * Successful authentication creates an authenticated user authentication token
         */
        CustomUsernamePasswordAuthenticationToken authenticationResult = new CustomUsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());

        authenticationResult.setDetails(authenticationToken.getDetails());
        /*
         * save info to SecurityContext
         */
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        /**
         * Specifies that the authentication processor
         */
        return CustomUsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
