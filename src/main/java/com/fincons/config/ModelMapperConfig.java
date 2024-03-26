package com.fincons.config;

import com.fincons.entity.QuizResults;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapperStandard() {
        return new ModelMapper();
    }

    @Bean
    public ModelMapper modelMapperForQuizResults(){
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addMappings(new PropertyMap<QuizResults, QuizResults>() {
            @Override
            protected void configure() {
                skip(destination.getQuiz().getQuizResults());
                skip(destination.getUser().getQuizResults());
            }
        });
        return modelMapper;
    }
}
