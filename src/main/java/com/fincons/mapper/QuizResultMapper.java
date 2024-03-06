package com.fincons.mapper;

import com.fincons.dto.QuizResultsDto;
import com.fincons.entity.QuizResults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class QuizResultMapper {


    private final ModelMapper modelMapperStandard = new ModelMapper();

    private QuizMapper quizMapper;

    private UserAndRoleMapper userAndRoleMapper;

    public QuizResultsDto mapQuizResultsEntityToDto(QuizResults quizResults) {
        QuizResultsDto quizResultsDto = new QuizResultsDto();
        quizResultsDto.setId(quizResults.getId());
        quizResultsDto.setQuiz(quizMapper.mapQuizToQuizDto(quizResults.getQuiz()));
        quizResultsDto.setUser(userAndRoleMapper.userToUserDto(quizResults.getUser()));
        return quizResultsDto;
    }
    public QuizResults mapQuizResultsDtoToEntity(QuizResultsDto quizResultsDto) {
        QuizResults quizResults = new QuizResults();
        quizResults.setId(quizResultsDto.getId());
        quizResults.setQuiz(quizMapper.mapQuizDtoToQuizEntity(quizResultsDto.getQuiz()));
        quizResults.setUser(userAndRoleMapper.dtoToUser(quizResultsDto.getUser()));
        return quizResults;
    }

    public List<QuizResultsDto> mapQREntityToQRDTOSList(List<QuizResults> quizResultsList){
        return quizResultsList
                .stream()
                .map(quizResults -> mapQuizResultsEntityToDto(quizResults))
                .toList();
    }

}
