package com.agilent.cdsa.config;

import com.agilent.cdsa.common.ConfigConstant;
import com.agilent.cdsa.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

/**
 * Spring Security Config
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserServiceImpl userServiceImpl;

    /**
     * Http安全配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.formLogin().loginPage(ConfigConstant.REQUEST_LOGIN_PAGE_URL)
                .loginProcessingUrl(ConfigConstant.LOGIN_FORM_SUBMIT_URL)
                .successHandler(new AuthenticationSuccessHandler() {

                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                        Authentication authentication) throws IOException, ServletException {
                        response.setContentType("application/json;charset=utf-8");

                        RequestCache cache = new HttpSessionRequestCache();
                        SavedRequest savedRequest = cache.getRequest(request, response);
                        String url = savedRequest.getRedirectUrl();

                        response.sendRedirect(url);

                    }
                })
//                .defaultSuccessUrl(ConfigConstant.DEFAULT_LOGIN_SUCCESSFUL_REQUEST_URL, ConfigConstant.ALWAYS_USE_DEFAULT_LOGIN_SUCCESSFUL_REQUEST_URL)
                .failureHandler(failureHandler())
                .permitAll()
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
                        "/swagger-ui.html",
                        "/static/**"
                )
                .permitAll()
                .antMatchers("/", "/**/api/**", "/login", "/check") // access login or register without authorize
                .permitAll()
                .anyRequest()
                .authenticated();
        // 关闭 cookie
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //session管理,失效后跳转
        http.sessionManagement().invalidSessionUrl("/login");
        //退出时情况cookies
        http.logout().deleteCookies("JESSIONID");
        //解决中文乱码问题
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        http.addFilterBefore(filter, CsrfFilter.class);
    }

    SimpleUrlAuthenticationFailureHandler failureHandler() {
        SimpleUrlAuthenticationFailureHandler handler = new SimpleUrlAuthenticationFailureHandler();
        handler.setDefaultFailureUrl("/toRedirect");
        handler.setUseForward(true);
        return handler;
    }

    /**
     * 密码加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        /**
         * BCryptPasswordEncoder：相同的密码明文每次生成的密文都不同，安全性更高
         */
        return new CustomPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider bean = new DaoAuthenticationProvider();
        //返回错误信息提示，而不是Bad Credential
        bean.setHideUserNotFoundExceptions(true);
        //覆盖UserDetailsService类
        bean.setUserDetailsService(userServiceImpl);
        //覆盖默认的密码验证类
        bean.setPasswordEncoder(passwordEncoder());
        return bean;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(this.daoAuthenticationProvider());
    }
}
