package com.fincons.security;

import com.fincons.audit.ApplicationAuditAware;
import com.fincons.jwt.JwtAuthenticationFilter;
import com.fincons.jwt.JwtUnauthorizedAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
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

    @Value("${application.context}")
    private String applicationContext;

    @Value("${base.uri.version}")
    private String baseUriVersion;

    @Value("${base.authorization.uri}")
    private String baseAuthorizationUri;

    @Value("${register.student.uri}")
    private String registerStudentUri;

    @Value("${register.tutor.uri}")
    private String registerTutorUri;

    @Value("${register.admin.uri}")
    private String registerAdminUri;

    @Value("${login.uri}")
    private String loginUri;

    @Value("${course.base.uri}")
    private String courseBaseUri;

    @Value("${course.get-all-courses}")
    private String getAllCoursesUri;

    @Value("${lesson.base.uri}")
    private String lessonsBaseUri;

    @Value("${content.base.uri}")
    private String contentBaseUri;
    @Value("${course.getDedicatedCourses}")
    private String getDedicatedCourses;

    @Value("${ability.base.uri}")
    private String abilityBaseUri;

    @Value("${ability-user.base.uri}")
    private String abilityUserBaseUri;

    @Value("${ability-course.base.uri}")
    private String abilityCourseBaseUri;

    @Value("${lesson.base.uri}")
    private String lessonBaseUri;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http.csrf(AbstractHttpConfigurer::disable);


        //requests
        http.authorizeHttpRequests(auth  ->
                auth.requestMatchers(applicationContext + loginUri).permitAll()
                        .requestMatchers(applicationContext + registerTutorUri).hasRole("ADMIN")
                        .requestMatchers(applicationContext + registerStudentUri).permitAll()
                        .requestMatchers(applicationContext + registerAdminUri).permitAll()  // TODO To remove
                        .requestMatchers(applicationContext + getDedicatedCourses + "/**").authenticated()
                        .requestMatchers(applicationContext + lessonsBaseUri + "/**").authenticated()
                        .requestMatchers(applicationContext + contentBaseUri + "/**").authenticated()

                        /*
                         Filtri CRUD per l'amministratore:
                         applicationContext = /nfc-learning
                         courseBaseUri = v1/course
                         */
                        .requestMatchers(applicationContext + abilityUserBaseUri + "/**").authenticated()
                        .requestMatchers(applicationContext + courseBaseUri + "/**").hasRole("ADMIN")
                        .requestMatchers(applicationContext + abilityBaseUri + "/**").hasRole("ADMIN")
                        .requestMatchers(applicationContext + abilityCourseBaseUri + "/**").hasRole("ADMIN")
                        .requestMatchers(applicationContext + lessonBaseUri + "/**").hasRole("ADMIN")





                        .anyRequest().authenticated()
        ).httpBasic(Customizer.withDefaults());


        http
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationExeptionEntryPoint));

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
