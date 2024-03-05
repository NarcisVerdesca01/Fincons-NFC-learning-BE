package com.fincons.repository;

import com.fincons.entity.Lesson;
import com.fincons.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz,Long> {
    boolean existsByTitle(String title);
    boolean existsByLesson(Lesson lesson);
}
