package com.example.demo2.config;

import com.example.demo2.service.appUserService.AppUserService;
import com.example.demo2.service.appUserService.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class AppSecConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomSuccessHandler customSuccessHandler;

    @Autowired
    public AppUserService appUserService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService((UserDetailsService) appUserService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/", "/api/login", "/api/tutorials",
                "/error",
                "/favicon.ico",
                "/**/*.png",
                "/**/*.gif",
                "/**/*.svg",
                "/**/*.jpg",
                "/**/*.html",
                "/**/*.css",
                "/css/**",
                "/fonts/**",
                "/images/**",
                "/**/*.js").permitAll();
        http.authorizeRequests()
                .antMatchers( "/api/**").access("hasAnyRole('ROLE_ADMIN')")
                .antMatchers( "/api/tutorials/**").access("hasAnyRole('ROLE_USER')")

                .and().formLogin()
                .loginProcessingUrl("/check_url") // Submit URL
                .loginPage("/api/login")
                .successHandler(customSuccessHandler)
                .failureUrl("/api/login?error=true")//
                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .and().exceptionHandling().accessDeniedPage("/api/403")
                .and().authorizeRequests().anyRequest().authenticated();
        http.csrf().disable();
    }
}
