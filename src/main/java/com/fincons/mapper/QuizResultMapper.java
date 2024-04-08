package com.fincons.mapper;

import com.fincons.dto.QuizDto;
import com.fincons.dto.QuizResultsDto;
import com.fincons.entity.Quiz;
import com.fincons.entity.QuizResults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class QuizResultMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private QuizMapper quizMapper;

    @Autowired
    private UserAndRoleMapper userAndRoleMapper;

    public QuizResultsDto mapQuizResultsEntityToDto(QuizResults quizResults) {
       return modelMapper.map(quizResults, QuizResultsDto.class);
    }

    public QuizResults mapQuizResultsDtoToEntity(QuizResultsDto quizResultsDto) {
        return modelMapper.map(quizResultsDto, QuizResults.class);
    }


    public List<QuizResultsDto> mapQREntityToQRDTOSList(List<QuizResults> quizResultsList){
        return quizResultsList
                .stream()
                .map(this::mapQuizResultsEntityToDto)
                .toList();
    }

    public QuizDto mapQuizToQuizDto(Quiz quiz){
        return modelMapper.map(quiz, QuizDto.class);
    }

    public Quiz mapQuizDtoToQuizEntity(QuizDto quizDto){
        return modelMapper.map(quizDto, Quiz.class);
    }


}
