package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @JsonManagedReference
    private List<CourseLessonDto> courses;

    @JsonManagedReference
    private QuizDto quiz;

    @JsonManagedReference
    private ContentDto content;
    private LocalDateTime createDate;
    private LocalDateTime lastModified;
    private String createdBy;
    private String lastModifiedBy;

}
