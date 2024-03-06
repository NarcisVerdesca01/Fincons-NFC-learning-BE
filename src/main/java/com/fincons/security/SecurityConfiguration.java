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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http.csrf(AbstractHttpConfigurer::disable);


        //requests
        http.authorizeHttpRequests(auth  ->
                auth

                        .requestMatchers(applicationContext + loginUri).permitAll()
                        .requestMatchers(applicationContext + registerTutorUri).hasRole("ADMIN")
                        .requestMatchers(applicationContext + registerStudentUri).permitAll()
                        .requestMatchers(applicationContext + registerAdminUri).authenticated()  // TODO To remove
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
                        .requestMatchers(HttpMethod.PUT, applicationContext + courseBaseUri + "/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, applicationContext + courseBaseUri+ "/**").hasAnyRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, applicationContext + lessonBaseUri + "/**").hasAnyRole("ADMIN", "TUTOR", "STUDENT")
                        .requestMatchers(HttpMethod.POST, applicationContext + lessonBaseUri + "/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, applicationContext + lessonBaseUri + "/**").hasAnyRole("ADMIN","TUTOR")
                        .requestMatchers(HttpMethod.DELETE, applicationContext + lessonBaseUri + "/**").hasAnyRole("ADMIN")

                        //Associazione
                        .requestMatchers(HttpMethod.POST, applicationContext + courseLessonBaseUri + "/**").hasAnyRole("TUTOR")
                        .requestMatchers(HttpMethod.PUT, applicationContext + courseLessonBaseUri + "/**").hasAnyRole("TUTOR")
                        .requestMatchers(HttpMethod.DELETE, applicationContext + courseLessonBaseUri + "/**").hasAnyRole("TUTOR")
                        .requestMatchers(HttpMethod.GET, applicationContext + courseLessonBaseUri + "/**").hasAnyRole("ADMIN", "TUTOR", "STUDENT")


                                                //TODO -  Tutor CRUD on Quiz
                         .requestMatchers(HttpMethod.GET, applicationContext + quizBaseUri + "/**").hasAnyRole("ADMIN", "TUTOR", "STUDENT")
                        .requestMatchers(HttpMethod.POST, applicationContext + quizBaseUri + "/**").hasAnyRole("TUTOR")
                        .requestMatchers(HttpMethod.PUT, applicationContext + quizBaseUri + "/**").hasAnyRole("TUTOR")
                        .requestMatchers(HttpMethod.DELETE, applicationContext + quizBaseUri + "/**").hasAnyRole("TUTOR")

                        /*
                        .requestMatchers(HttpMethod.POST, applicationContext + quizResultStudentBaseUri + "/**").hasAnyRole("STUDENT")
                        .requestMatchers(HttpMethod.GET, applicationContext + quizResultStudentBaseUri + "/**").hasAnyRole("ADMIN", "TUTOR", "STUDENT")


                                                               //TODO - Associate Quiz to Student
                                       .requestMatchers(HttpMethod.GET, applicationContext +  + "/**").hasAnyRole("ADMIN", "TUTOR", "STUDENT")
                                       .requestMatchers(HttpMethod.POST, applicationContext +  + "/**").hasAnyRole("TUTOR")
                                       .requestMatchers(HttpMethod.PUT, applicationContext +  + "/**").hasAnyRole("TUTOR")
                                       .requestMatchers(HttpMethod.DELETE, applicationContext +  + "/**").hasAnyRole("TUTOR")


                        /*



                        //TODO STUDENT Read on Courses, Lessons, Quiz related to its technical profile
                        //TODO STUDENT CRUD on associated Quiz Answers

                        */

                        .anyRequest().authenticated()
        ).httpBasic(Customizer.withDefaults());


        http
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationExeptionEntryPoint));

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    /*
     .antMatchers("/api/authenticate").permitAll() // Endpoint di autenticazione accessibile a tutti
                .antMatchers(HttpMethod.GET, "/api/corsi/**").hasAnyRole("ADMIN", "TUTOR", "STUDENT")   FATTO
                .antMatchers(HttpMethod.POST, "/api/corsi").hasRole("ADMIN")                            FATTO
                .antMatchers(HttpMethod.PUT, "/api/corsi/**").hasRole("ADMIN")                          FATTO
                .antMatchers(HttpMethod.DELETE, "/api/corsi/**").hasRole("ADMIN")                       FATTO

                .antMatchers(HttpMethod.GET, "/api/lezioni/**").hasAnyRole("ADMIN", "TUTOR", "STUDENT") FATTO
                .antMatchers(HttpMethod.POST, "/api/lezioni").hasRole("TUTOR")                          FATTO
                .antMatchers(HttpMethod.PUT, "/api/lezioni/**").hasRole("TUTOR")                        FATTO
                .antMatchers(HttpMethod.DELETE, "/api/lezioni/**").hasRole("TUTOR")                     FATTO

                .antMatchers(HttpMethod.GET, "/api/quiz/**").hasAnyRole("ADMIN", "TUTOR", "STUDENT")    FATTO
                .antMatchers(HttpMethod.POST, "/api/quiz").hasRole("TUTOR")                             FATTO
                .antMatchers(HttpMethod.PUT, "/api/quiz/**").hasRole("TUTOR")                           FATTO
                .antMatchers(HttpMethod.DELETE, "/api/quiz/**").hasRole("TUTOR")                        FATTO

                .antMatchers(HttpMethod.GET, "/api/risposte-quiz/**").hasRole("STUDENT")
                .antMatchers(HttpMethod.POST, "/api/risposte-quiz").hasRole("STUDENT")
                .antMatchers(HttpMethod.PUT, "/api/risposte-quiz/**").hasRole("STUDENT")
                .antMatchers(HttpMethod.DELETE, "/api/risposte-quiz/**").hasRole("STUDENT")
                                .anyRequest().authenticated()
     */

















}
