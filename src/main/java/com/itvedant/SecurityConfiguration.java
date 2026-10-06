package com.itvedant;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    // 1. Password Encoder
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. In-Memory Users
    @Bean
    public UserDetailsManager userDetails() {

        UserDetails user1 = User
                .withUsername("admin")
                .password(encoder().encode("admin@123"))
                .roles("ADMIN")
                .build();

        UserDetails user2 = User
                .withUsername("ketan")
                .password(encoder().encode("ketan@123"))
                .roles("USER")
                .build();

        UserDetails user3 = User
                .withUsername("amit")
                .password(encoder().encode("amit@123"))
                .roles("ADMIN", "USER")
                .build();

        return new InMemoryUserDetailsManager(user1, user2, user3);
    }

    // 3. Security Configuration
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(auth -> auth

                // ADMIN only
                .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN")

                // Other endpoints are public
                .anyRequest().permitAll()
        );

        http.httpBasic(Customizer.withDefaults());

        return http.build();
    }
}