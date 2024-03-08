package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(scope = LessonDto.class, generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class LessonDto {

    private long id;
    private String title;
    private List<CourseLessonDto> courses;
    private QuizDto quiz;
    private ContentDto content;
    private String backgroundImage;

    private LocalDateTime createDate;
    private LocalDateTime lastModified;
    private String createdBy;
    private String lastModifiedBy;


}
