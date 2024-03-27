package com.fincons.repository;

import com.fincons.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

    boolean existsByIdAndDeletedFalse(long id);

    Content findByIdAndDeletedFalse(long id);
    List<Content> findAllByDeletedFalse();

    List<Content> findAllByDeletedFalse();

    //metodo per contenuti non associati a nessuna lezione
    List<Content> findAllByDeletedFalseAndLessonIsNull();


}
