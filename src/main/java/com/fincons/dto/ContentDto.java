package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(scope = ContentDto.class, generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class ContentDto {

    private Long id;
    private String typeContent;
    private String content;
    private LessonDto lesson;
    private boolean deleted;

}
