package com.fincons.mapper;

import com.fincons.dto.AnswerDto;
import com.fincons.entity.Answer;
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
