package com.agilent.cdsa.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 继承密码
 */
public class CustomPasswordEncoder extends BCryptPasswordEncoder {

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        boolean matches = super.matches(rawPassword, encodedPassword);
        return matches;
    }
}
