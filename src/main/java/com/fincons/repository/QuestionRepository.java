package com.fincons.repository;

import com.fincons.entity.Question;
import com.fincons.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

}
