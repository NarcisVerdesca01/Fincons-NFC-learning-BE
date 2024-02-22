package com.fincons.security;

import com.fincons.jwt.JwtAuthenticationFilter;
import com.fincons.jwt.JwtUnauthorizedAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {


    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Autowired
    private JwtUnauthorizedAuthenticationEntryPoint authenticationExeptionEntryPoint;

    @Autowired
    private   JwtAuthenticationFilter jwtAuthFilter;




    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:3000");
            }
        };
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http.csrf(AbstractHttpConfigurer::disable);


        //requests
        http.authorizeHttpRequests(auth  ->
                auth.requestMatchers("/nfc-learning/auth/**").permitAll()
                        .anyRequest().authenticated()
        );


        http
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationExeptionEntryPoint));

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
