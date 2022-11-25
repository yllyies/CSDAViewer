package com.agilent.csda.config;

import com.agilent.csda.acl.service.impl.UserServiceImpl;
import com.agilent.csda.component.CustomUsernamePasswordAuthenticationConfig;
import com.agilent.csda.common.ConfigConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import javax.sql.DataSource;

/**
 * Spring Security Config
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private CustomUsernamePasswordAuthenticationConfig customUsernamePasswordAuthenticationConfig;

    /**
     * 用户认证配置
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /**
         * 指定用户认证时，默认从哪里获取认证用户信息
         */
        auth.userDetailsService(userServiceImpl);
    }

    /**
     * Http安全配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .apply(customUsernamePasswordAuthenticationConfig)
                .and()
                .formLogin()
                .loginPage(ConfigConstant.REQUEST_LOGIN_PAGE_URL)
                .loginProcessingUrl(ConfigConstant.LOGIN_FORM_SUBMIT_URL)
                .defaultSuccessUrl(ConfigConstant.DEFAULT_LOGIN_SUCCESSFUL_REQUEST_URL, ConfigConstant.ALWAYS_USE_DEFAULT_LOGIN_SUCCESSFUL_REQUEST_URL)
                .permitAll()
                .and()
//            .logout()
//                .logoutUrl(ConfigConstant.LOGOUT_URL)
//                .logoutSuccessUrl(ConfigConstant.LOGOUT_SUCCESSFUL_REQUEST_URL)
//                .logoutRequestMatcher(getLogoutRequestMatchers())
//                .permitAll()
//                .and()
                .rememberMe()
                .tokenRepository(getPersistentTokenRepository())
                .tokenValiditySeconds(ConfigConstant.REMEMBER_ME_SECOND)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, // access static resource without authorize
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.map",
                        "/**/*.woff2",
                        "/**/*.png",
                        "/**/*.jpg",
                        "/**/*.ico",
                        "/**/*.js",
                        "/swagger-resources/**",
                        "/static/**",
                        "/v2/api-docs/**"
                )
                .permitAll()
                .antMatchers("/", "/login", "/check", "/user/**") // access login or register without authorize
                .permitAll()
                .anyRequest()
                .authenticated();
    }

    /**
     * 密码加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        /**
         * BCryptPasswordEncoder：相同的密码明文每次生成的密文都不同，安全性更高
         */
        return new BCryptPasswordEncoder();
    }

    /**
     * 指定保存用户登录“记住我”功能的Token的方法：
     * 默认是使用InMemoryTokenRepositoryImpl将Token放在内存中，
     * 如果使用JdbcTokenRepositoryImpl，会创建persistent_logins数据库表，并将Token保存到该表中。
     */
    @Bean
    public PersistentTokenRepository getPersistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        /**
         * 系统启动时自动创建表，只需要在第一次启动系统时创建即可，因此这行代码只在需要创建表时才启用
         */
//        jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }

    /**
     * 自定义退出登录的RequestMatcher
     */
    private OrRequestMatcher getLogoutRequestMatchers() {
        /**
         * 用户退出登录时，匹配GET请求/logout和POST请求/logout，使得这两种请求都执行退出登录操作
         * 默认情况（未禁用跨域请求伪造，且自定义用户登录页面）下，只对POST请求/logout才执行退出登录操作
         */
        AntPathRequestMatcher getLogoutRequestMatcher = new AntPathRequestMatcher(ConfigConstant.LOGOUT_URL, "GET");
        AntPathRequestMatcher postLogoutRequestMatcher = new AntPathRequestMatcher(ConfigConstant.LOGOUT_URL, "POST");
        return new OrRequestMatcher(getLogoutRequestMatcher, postLogoutRequestMatcher);
    }
}
