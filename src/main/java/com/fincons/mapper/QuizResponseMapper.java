package com.fincons.mapper;

import com.fincons.dto.QuizResponseDto;
import com.fincons.entity.QuizResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class QuizResponseMapper {


    private final ModelMapper modelMapperStandard = new ModelMapper();

    @Autowired
    private QuizMapper quizMapper;
    @Autowired
    private UserAndRoleMapper userAndRoleMapper;
    @Autowired
    private QuestionMapper questionMapper;


    public QuizResponseDto mapQuizResponseEntityToDto(QuizResponse quizResponse) {
        QuizResponseDto quizResponseDto = new QuizResponseDto();
        quizResponseDto.setId(quizResponse.getId());
        quizResponseDto.setQuiz(quizMapper.mapQuizToQuizDto(quizResponse.getQuiz()));
        quizResponseDto.setQuestion(questionMapper.mapQuestionToQuestionDto(quizResponse.getQuestion()));
        quizResponseDto.setUser(userAndRoleMapper.userToUserDto(quizResponse.getUser()));
        quizResponseDto.setChosenAnswers(quizResponse.getChosenAnswers());
        quizResponseDto.setScoreOfStudentForQuestion(quizResponse.getScoreOfStudentForQuestion());
        return quizResponseDto;
    }
    public QuizResponse mapQuizResponseDtoToEntity(QuizResponseDto quizResponseDto) {
        QuizResponse quizResponse = new QuizResponse();
        quizResponse.setId(quizResponseDto.getId());
        quizResponse.setQuiz(quizMapper.mapQuizDtoToQuizEntity(quizResponseDto.getQuiz()));
        quizResponse.setQuestion(questionMapper.mapQuestionDtoToQuestionEntity(quizResponseDto.getQuestion()));
        quizResponse.setUser(userAndRoleMapper.dtoToUser(quizResponseDto.getUser()));
        quizResponse.setChosenAnswers(quizResponseDto.getChosenAnswers());
        quizResponse.setScoreOfStudentForQuestion(quizResponseDto.getScoreOfStudentForQuestion());
        return quizResponse;
    }


    public List<QuizResponseDto> mapQResponseEntityToQResponseDTOSList(List<QuizResponse> quizResponseList){
        return quizResponseList
                .stream()
                .map(this::mapQuizResponseEntityToDto)
                .toList();
    }


}
