package com.fincons.mapper;

import com.fincons.dto.AnswerDto;
import com.fincons.dto.QuestionDto;
import com.fincons.entity.Answer;
import com.fincons.entity.Question;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AnswerMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public AnswerDto mapAnswerToAnswerDto(Answer answer){
        return modelMapper.map(answer, AnswerDto.class);
    }

    public Answer mapAnswerDtoToAnswerEntity(AnswerDto answerDto){
        return modelMapper.map(answerDto, Answer.class);
    }
}
