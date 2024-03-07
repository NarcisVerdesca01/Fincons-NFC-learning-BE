package com.fincons.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
    @OneToOne(mappedBy= "content")
    private Lesson lesson;



}
