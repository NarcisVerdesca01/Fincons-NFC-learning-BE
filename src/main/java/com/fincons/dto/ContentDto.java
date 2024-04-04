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

    private Long id;

    private String typeContent;

    private String content;

    @JsonIgnoreProperties("content")
    private LessonDto lesson;

    private boolean deleted;

}
