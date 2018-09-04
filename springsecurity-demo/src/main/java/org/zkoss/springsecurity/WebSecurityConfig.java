package org.zkoss.springsecurity;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //make zk au work
        //ZK already has built-in CSRF token when sending a request, please refer to https://www.zkoss.org/wiki/ZK%20Developer's%20Reference/Security%20Tips/Cross-site%20Request%20Forgery
        http.csrf().disable();
        http.authorizeRequests()
            .antMatchers("/zkau/web/**/js/**","/zkau/web/**/zul/css/**","/zkau/web/**/img/**")
            .permitAll()
            // block direct access to class path web resources
            .antMatchers("/zkau/web/**/**.zul").denyAll()
            .mvcMatchers("/","/login","/logout").permitAll()
            .mvcMatchers("/secure").hasRole("USER")
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login").defaultSuccessUrl("/secure/main")
            .and()
            .logout().logoutUrl("/logout").logoutSuccessUrl("/");
    }

    @Bean
    @Override
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