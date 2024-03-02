package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class LessonDto {

    private long id;

    private String title;

    @JsonIgnore
    private List<CourseLessonDto> courses;

    @JsonIgnore
    private QuizDto quiz;

    @JsonIgnore
    private ContentDto content;

    private LocalDateTime createDate;

    private LocalDateTime lastModified;

    private String createdBy;

    private String lastModifiedBy;

}
