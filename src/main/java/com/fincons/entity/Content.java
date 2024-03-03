package com.fincons.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contents")
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "typeContent")
    private String typeContent;

    @Column(name = "content", length = 20971520)
    private String content;

    //2. LEZIONE - CONTENUTO 1:1
    @OneToOne
    @JsonBackReference
    @JoinColumn(name = "id_lesson")
    private Lesson lesson;



}
