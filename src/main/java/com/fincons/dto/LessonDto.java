package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class LessonDto {

    private long id;
    private String title;
    @JsonIgnoreProperties("lesson")
    private List<CourseLessonDto> courseLessons;
    @JsonIgnoreProperties("lesson")
    private QuizDto quiz;
    @JsonIgnoreProperties("lesson")
    private ContentDto content;
    private String backgroundImage;
    private boolean deleted;
    private LocalDateTime createDate;
    private LocalDateTime lastModified;
    private String createdBy;
    private String lastModifiedBy;


}
