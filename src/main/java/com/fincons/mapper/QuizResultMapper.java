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
        QuizResultsDto quizResultsDto = new QuizResultsDto();
        quizResultsDto.setId(quizResults.getId());
        quizResultsDto.setQuiz(quizMapper.mapQuizToQuizDto(quizResults.getQuiz()));
        quizResultsDto.setUser(userAndRoleMapper.userToUserDto(quizResults.getUser()));
        quizResultsDto.setTotalScore(quizResults.getTotalScore());
        quizResultsDto.setWhenDone(quizResults.getWhenDone());
        quizResultsDto.setDeleted(quizResults.isDeleted());
        return quizResultsDto;
    }

    public QuizResults mapQuizResultsDtoToEntity(QuizResultsDto quizResultsDto) {
        QuizResults quizResults = new QuizResults();
        quizResults.setId(quizResultsDto.getId());
        quizResults.setQuiz(quizMapper.mapQuizDtoToQuizEntity(quizResultsDto.getQuiz()));
        quizResults.setUser(userAndRoleMapper.dtoToUser(quizResultsDto.getUser()));
        quizResults.setTotalScore(quizResultsDto.getTotalScore());
        quizResults.setWhenDone(quizResultsDto.getWhenDone());
        quizResults.setDeleted(quizResultsDto.isDeleted());

        return quizResults;
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
