package com.fincons.mapper;

import com.fincons.dto.ContentDto;
import com.fincons.entity.Content;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ContentMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public ContentDto mapContentToContentDto(Content content){
        return modelMapper.map(content, ContentDto.class);
    }

    public Content mapContentDtoToContentEntity(ContentDto contentDto){
        return modelMapper.map(contentDto, Content.class);
    }

}
