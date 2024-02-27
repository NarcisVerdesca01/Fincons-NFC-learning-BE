package com.fincons.entity;

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
    private long id;

    @Column(name = "typeContent")
    private String typeContent;

    @Column(name = "content", columnDefinition = "VARBINARY(MAX)")
    private byte[] content;

    //2. LEZIONE - CONTENUTO 1:1
    @OneToOne(mappedBy = "content", cascade = CascadeType.ALL)
    private Lesson lesson;



}
