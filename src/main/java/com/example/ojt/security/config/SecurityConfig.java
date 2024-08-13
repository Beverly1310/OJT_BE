package com.example.ojt.security.config;

import com.example.ojt.security.jwt.JWTAuthTokenFilter;
import com.example.ojt.security.principle.AccountDetailsServiceCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {
    @Autowired
    private JWTAuthTokenFilter authTokenFilter;
    @Autowired
    private SecurityAuthenticationEntryPoint entryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private AccountDetailsServiceCustom detailsServiceCustom;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(detailsServiceCustom);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomDeniedHandler();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(httpSecurityCorsConfigurer ->
                        httpSecurityCorsConfigurer.
                                configurationSource(request -> {
                                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                                    corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173/"));
                                    corsConfiguration.setAllowedMethods(List.of("*"));
                                    corsConfiguration.setAllowedHeaders(List.of("*"));
                                    corsConfiguration.setAllowCredentials(true);
                                    corsConfiguration.setExposedHeaders(List.of("*"));
                                    return corsConfiguration;
                                }))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
//                        auth.requestMatchers("/admin/**").hasRole("ADMIN"))
                                auth.requestMatchers(
                                                "/api.myservice.com/v1/admin/**").permitAll()
                                        .requestMatchers("/api.myservice.com/v1/user/**").hasAuthority("ROLE_USER")
                                        .requestMatchers("/api.myservice.com/v1/manager/**").hasAuthority("ROLE_MANAGER")
                                        .requestMatchers("/api.myservice.com/v1/user-manager/**", "/user-client/**").hasAnyAuthority("ROLE_USER", "ROLE_MANAGER")
                                        .anyRequest().permitAll() // tất cả quyền
                )
                .authenticationProvider(authenticationProvider())
                .exceptionHandling(e -> e.authenticationEntryPoint(entryPoint).accessDeniedHandler(accessDeniedHandler()))
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


}
