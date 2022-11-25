package com.agilent.csda.component;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Custom UsernamePasswordAuthenticationToken
 */
public class CustomUsernamePasswordAuthenticationToken extends AbstractAuthenticationToken {
    /**
     * username
     */
    private final Object principal;

    /**
     * password
     */
    private Object credentials;

    /*
     * create unauthenticated token
     */
    public CustomUsernamePasswordAuthenticationToken(Object principal, Object credentials) {
        super((Collection) null);
        this.principal = principal;
        this.credentials = credentials;
        this.setAuthenticated(false);
    }

    /*
     * create authenticated token
     */
    public CustomUsernamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }

}
