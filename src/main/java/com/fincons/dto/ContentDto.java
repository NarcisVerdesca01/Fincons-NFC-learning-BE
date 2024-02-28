package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContentDto {

    private long id;

    private String typeContent;

    private byte[] content;

    @JsonIgnoreProperties("content")
    private LessonDto lesson;
}
