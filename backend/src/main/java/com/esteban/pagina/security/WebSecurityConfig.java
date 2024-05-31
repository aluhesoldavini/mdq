package com.esteban.pagina.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailService customUserDetailService;

    public WebSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, CustomUserDetailService customUserDetailService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customUserDetailService = customUserDetailService;
    }

    @Bean
    public SecurityFilterChain applicationSecurity(HttpSecurity http) throws Exception {
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http
                .csrf(AbstractHttpConfigurer::disable).cors(corsCustomizer -> {
                    corsCustomizer
                            .configurationSource(request -> {
                                CorsConfiguration corsConfiguration = new CorsConfiguration();
                                corsConfiguration.addAllowedOrigin("*");
                                corsConfiguration.addAllowedMethod("GET");
                                corsConfiguration.addAllowedMethod("POST");
                                corsConfiguration.addAllowedMethod("PUT");
                                corsConfiguration.addAllowedMethod("DELETE");
                                corsConfiguration.addAllowedMethod("OPTIONS");
                                corsConfiguration.addAllowedHeader("Authorization");
                                corsConfiguration.addAllowedHeader("Content-Type");

                                return corsConfiguration;
                            });
                })
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                    .requestMatchers(HttpMethod.GET,
                            "/api/v1/category",
                            "/api/v1/category/*",
                            "/api/v1/product",
                            "/api/v1/product/**",
                            "/api/v1/contact",
                            "/api/v1/contact/*",
                            "/api/v1/auth/checkrole").permitAll()
                    .requestMatchers("/api/v1/user", "/api/v1/user/**").hasAuthority("Jefe")
                    .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                    .requestMatchers(HttpMethod.POST,
                            "/api/v1/category/create",
                            "/api/v1/product/create")
                    .hasAnyAuthority("Encargado", "Jefe")
                    .requestMatchers(HttpMethod.PUT,
                            "/api/v1/category/update",
                            "/api/v1/product/update",
                            "/api/v1/contact/update")
                    .hasAnyAuthority("Encargado", "Jefe")
                    .requestMatchers(HttpMethod.DELETE,
                            "/api/v1/category/delete/*",
                            "/api/v1/product/delete/*")
                    .hasAnyAuthority("Encargado", "Jefe")
                    .anyRequest()
                    .authenticated())
                    .sessionManagement((sessionManagement) ->
                            sessionManagement
                                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http
                .getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }
}
