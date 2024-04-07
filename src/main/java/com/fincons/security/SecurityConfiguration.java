package com.fincons.security;

import com.fincons.jwt.JwtAuthenticationFilter;
import com.fincons.jwt.JwtUnauthorizedAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    @Value("${course-lesson.base.uri}")
    private String courseLessonBaseUri;

    @Value("${detail.userdto}")
    private String getUserDtoByEmail;

    @Value("${quiz.base.uri}")
    private String quizBaseUri;

    @Value("${answer.base.uri}")
    private String answerBaseUri;

    @Value("${question.base.uri}")
    private String questionBaseUri;

    @Value("${quiz-result-student.base.uri}")
    private String quizResultStudentBaseUri;

    @Value("${quiz-result-student.list.singleStudent}")
    private String quizResultStudentListSingleStudent;

    @Value("${quiz-result-student.check}")
    private String quizResultStudentCheck;

    @Value("${quiz-response.base.uri}")
    private String quizResponseBaseUri;

    @Value("${quiz.get-all-quiz-response}")
    private String quizGetAllQuizResponse;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(auth  ->

                auth
                        .requestMatchers(applicationContext + loginUri).permitAll()
                        .requestMatchers(applicationContext + registerTutorUri).hasRole("ADMIN")
                        .requestMatchers(applicationContext + registerStudentUri).permitAll()
                        .requestMatchers(applicationContext + getUserDtoByEmail).permitAll()

                        .requestMatchers(HttpMethod.GET, applicationContext + abilityBaseUri + "/**").hasAnyRole("ADMIN", "TUTOR", "STUDENT")
                        .requestMatchers(HttpMethod.POST, applicationContext + abilityBaseUri + "/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, applicationContext + abilityBaseUri + "/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, applicationContext + abilityBaseUri + "/**").hasAnyRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, applicationContext + abilityUserBaseUri + "/**").authenticated()
                        .requestMatchers(HttpMethod.POST, applicationContext + abilityUserBaseUri + "/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, applicationContext + abilityUserBaseUri + "/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, applicationContext + abilityUserBaseUri + "/**").authenticated()

                        .requestMatchers(HttpMethod.GET, applicationContext + contentBaseUri + "/**").hasAnyRole("ADMIN", "TUTOR", "STUDENT")
                        .requestMatchers(HttpMethod.POST, applicationContext + contentBaseUri + "/**").hasAnyRole("ADMIN","TUTOR")
                        .requestMatchers(HttpMethod.PUT, applicationContext + contentBaseUri + "/**").hasAnyRole("ADMIN","TUTOR")
                        .requestMatchers(HttpMethod.DELETE, applicationContext + contentBaseUri + "/**").hasAnyRole("ADMIN", "TUTOR")

                        .requestMatchers(HttpMethod.GET, applicationContext + courseBaseUri + "/**").hasAnyRole("ADMIN", "TUTOR", "STUDENT")
                        .requestMatchers(HttpMethod.POST, applicationContext + courseBaseUri + "/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, applicationContext + courseBaseUri + "/delete").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, applicationContext + courseBaseUri + "/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, applicationContext + courseBaseUri+ "/**").hasAnyRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, applicationContext + lessonBaseUri + "/**").hasAnyRole("ADMIN", "TUTOR", "STUDENT")
                        .requestMatchers(HttpMethod.POST, applicationContext + lessonBaseUri + "/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, applicationContext + lessonBaseUri + "/delete").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, applicationContext + lessonBaseUri + "/update").hasAnyRole("ADMIN","TUTOR")
                        .requestMatchers(HttpMethod.DELETE, applicationContext + lessonBaseUri + "/**").hasAnyRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, applicationContext + courseLessonBaseUri + "/**").hasAnyRole("ADMIN", "TUTOR", "STUDENT")
                        .requestMatchers(HttpMethod.POST, applicationContext + courseLessonBaseUri + "/**").hasAnyRole("TUTOR")
                        .requestMatchers(HttpMethod.PUT, applicationContext + courseLessonBaseUri + "/**").hasAnyRole("TUTOR")
                        .requestMatchers(HttpMethod.DELETE, applicationContext + courseLessonBaseUri + "/**").hasAnyRole("TUTOR")

                        .requestMatchers(HttpMethod.GET, applicationContext + questionBaseUri + "/**").hasAnyRole("ADMIN", "TUTOR", "STUDENT")
                        .requestMatchers(HttpMethod.POST, applicationContext + questionBaseUri + "/**").hasAnyRole("TUTOR")
                        .requestMatchers(HttpMethod.PUT, applicationContext + questionBaseUri + "/**").hasAnyRole("TUTOR")
                        .requestMatchers(HttpMethod.DELETE, applicationContext + questionBaseUri + "/**").hasAnyRole("TUTOR")

                        .requestMatchers(HttpMethod.GET, applicationContext + quizBaseUri + "/**").hasAnyRole("ADMIN", "TUTOR", "STUDENT")
                        .requestMatchers(HttpMethod.POST, applicationContext + quizBaseUri + "/**").hasAnyRole("TUTOR")
                        .requestMatchers(HttpMethod.PUT, applicationContext + quizBaseUri + "/**").hasAnyRole("TUTOR")
                        .requestMatchers(HttpMethod.DELETE, applicationContext + quizBaseUri + "/**").hasAnyRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, applicationContext + quizResultStudentBaseUri + "/calculate-and-save").hasAnyRole("STUDENT")
                        .requestMatchers(HttpMethod.PUT, applicationContext + quizResultStudentBaseUri + "/quiz-redo").hasAnyRole("STUDENT")
                        .requestMatchers(HttpMethod.GET, applicationContext + quizResultStudentListSingleStudent ).hasAnyRole("ADMIN","TUTOR","STUDENT")
                        .requestMatchers(HttpMethod.GET, applicationContext + quizResultStudentCheck ).hasAnyRole("STUDENT")
                        .requestMatchers(HttpMethod.GET, applicationContext + quizResultStudentBaseUri + "/**").hasAnyRole("ADMIN", "TUTOR")
                        .requestMatchers(HttpMethod.DELETE, applicationContext + quizResultStudentBaseUri + "/**").hasAnyRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, applicationContext + quizGetAllQuizResponse + "/**").hasAnyRole("ADMIN", "TUTOR")

                        .anyRequest().authenticated()

        ).httpBasic(Customizer.withDefaults());
        http
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationExeptionEntryPoint));

        return http.build();
    }


}
