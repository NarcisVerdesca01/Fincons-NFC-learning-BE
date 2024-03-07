package com.fincons.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title",nullable = false, length = 20971520)
    private String title;

    @OneToMany(mappedBy = "lesson",cascade = CascadeType.ALL)
    private List<CourseLesson> courses;

    //3. LEZIONE - QUIZ 1:1
    @OneToOne(mappedBy = "lesson", cascade = CascadeType.ALL)
    private Quiz quiz;

    //2. LEZIONE - CONTENUTO 1:1
    @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "content_Id")
    private Content content;

    @Column(name = "backgroundImage")
    private String backgroundImage;


    @CreatedDate
    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModified;


    @CreatedBy
    @Column(
            nullable = false,
            updatable = false
    )
    private String createdBy;

    @LastModifiedBy
    @Column(insertable = false)
    private String lastModifiedBy;

    public Lesson(Lesson lesson) {
        this.title= lesson.getTitle();
        this.courses= lesson.getCourses();
        this.quiz= lesson.getQuiz();
        this.content= lesson.getContent();
        this.createDate= lesson.getCreateDate();
        this.lastModified=lesson.getLastModified();
        this.lastModifiedBy= lesson.getLastModifiedBy();
    }
}
