package com.fincons.mapper;

import com.fincons.dto.QuizDto;
import com.fincons.entity.Quiz;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class QuizMapper {



    private static final ModelMapper modelMapper = new ModelMapper();
    public QuizDto mapQuizToQuizDto(Quiz quiz){
        return modelMapper.map(quiz, QuizDto.class);
    }
    public Quiz mapDtoToQuiz(QuizDto quizDto){
        return modelMapper.map(quizDto, Quiz.class);
    }


}
