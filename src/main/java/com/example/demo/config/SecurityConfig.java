package com.example.demo.config;

import com.example.demo.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, @Value("${app.frontend.callback:http://localhost:3000/oauth2/redirect}") String frontendCallback) throws Exception {
        SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler(frontendCallback + "?token=");
        http
            .csrf().disable()
            .authorizeHttpRequests(authorize -> authorize
                .antMatchers("/auth/**", "/oauth2/**", "/h2-console/**", "/login", "/error").permitAll()
                .antMatchers("/files/upload", "/forum/**", "/chat/**").authenticated()
                .anyRequest().authenticated()
            )
            /* .oauth2Login(oauth -> oauth
                .successHandler((request, response, authentication) -> {
                  
                    String email = authentication.getName();
                    String token = oAuth2UserService.createOrGetToken(email);
                    response.sendRedirect(frontendCallback + token);
                })
                .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
            ) */
            .logout().permitAll();

        http.headers().frameOptions().disable();

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}