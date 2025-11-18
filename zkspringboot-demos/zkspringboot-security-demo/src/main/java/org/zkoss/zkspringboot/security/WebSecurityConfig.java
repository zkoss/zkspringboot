package org.zkoss.zkspringboot.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.DispatcherTypeRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import jakarta.servlet.DispatcherType;

/**
 * This is an example of minimal configuration for ZK + Spring Security, we open as less access as possible to run a ZK-based application.
 * Please understand the configuration and modify it upon your requirement.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private static final String ZUL_FILES = "/zkau/web/**/*.zul";
    private static final String ZK_RESOURCES = "/zkres/**";
    // allow desktop cleanup after logout or when reloading login page
    private static final String REMOVE_DESKTOP_REGEX = "/zkau\\?dtid=.*&cmd_0=rmDesktop&.*";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // you need to disable spring CSRF to make ZK AU pass security filter
        // ZK already sends an AJAX request with a built-in CSRF token,
        // please refer to https://www.zkoss.org/wiki/ZK%20Developer's%20Reference/Security%20Tips/Cross-site%20Request%20Forgery
        //https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#disable-csrf
        //http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
        http.csrf(csrf -> csrf.csrfTokenRepository
                (CookieCsrfTokenRepository.withHttpOnlyFalse())
        );
        http.authorizeHttpRequests(matcherRegistry -> {
            // allow default error dispatcher
            matcherRegistry.requestMatchers(
                    new AndRequestMatcher(
                            new DispatcherTypeRequestMatcher(DispatcherType.ERROR),
                            AntPathRequestMatcher.antMatcher("/error")
                    )
            ).permitAll();
            // allow forwarded access to zul under class path web resource folder
            matcherRegistry.requestMatchers(
                    new AndRequestMatcher(
                            new DispatcherTypeRequestMatcher(DispatcherType.FORWARD),
                            AntPathRequestMatcher.antMatcher(ZUL_FILES)
                    )
            ).permitAll();

            // block direct access to zul under class path web resource folder
            matcherRegistry.requestMatchers(AntPathRequestMatcher.antMatcher(ZUL_FILES)).denyAll();
            // allow zk resources requests
            matcherRegistry.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, ZK_RESOURCES)).permitAll();
            // allow desktop remove request
            matcherRegistry.requestMatchers(RegexRequestMatcher.regexMatcher(HttpMethod.GET, REMOVE_DESKTOP_REGEX)).permitAll();
            // allow desktop cleanup from ZATS
            matcherRegistry.requestMatchers(req -> "rmDesktop".equals(req.getParameter("cmd_0"))).permitAll();
            //permit the URL for login and logout
            matcherRegistry.requestMatchers(AntPathRequestMatcher.antMatcher("/login"), AntPathRequestMatcher.antMatcher("/logout")).permitAll();
            // configure secure pages
            matcherRegistry.requestMatchers(AntPathRequestMatcher.antMatcher("/secure")).hasRole("USER");
            // allow favicon.ico
            matcherRegistry.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/favicon.ico")).permitAll();
            //enforce all requests to be authenticated, just permit some paths configured below
            matcherRegistry.anyRequest().authenticated();
        })
        .formLogin(formLoginConfigurer -> formLoginConfigurer.loginPage("/login")
                .defaultSuccessUrl("/secure/main", true)) //specify true, so spring won't redirect a user to his previously requested page
        .logout(logoutConfigurer -> logoutConfigurer.logoutUrl("/logout").logoutSuccessUrl("/"));

        return http.build();
    }

    /**
     * Creates an {@link InMemoryUserDetailsManager} for demo/testing purposes only. DON'T use this in production, see: {@link User#withUserDetails}!
     * @return userDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("password")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }
}