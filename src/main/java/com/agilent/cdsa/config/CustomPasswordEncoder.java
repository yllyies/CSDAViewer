package com.agilent.cdsa.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * SpringSecurity 密码配置类
 */
public class CustomPasswordEncoder extends BCryptPasswordEncoder {

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        boolean matches = super.matches(rawPassword, encodedPassword);
        return matches;
    }
}
