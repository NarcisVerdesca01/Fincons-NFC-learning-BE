package com.fincons.mapper;


import com.fincons.dto.QuestionDto;
import com.fincons.entity.Question;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class QuestionMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public QuestionDto mapQuestionToQuestionDto(Question question){
        return modelMapper.map(question, QuestionDto.class);
    }

    public Question mapQuestionDtoToQuestionEntity(QuestionDto questionDto){
        return modelMapper.map(questionDto, Question.class);
    }
}
