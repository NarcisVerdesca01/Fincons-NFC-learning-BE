package com.fincons.repository;

import com.fincons.entity.Answer;
import com.fincons.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer,Long> {

    boolean existsByIdAndDeletedFalse(long id);

    Answer findByIdAndDeletedFalse(long id);

    List<Answer> findAllByDeletedFalse();

    List<Answer> findAllByDeletedFalseAndQuestionIsNull();

    boolean existsByTextAndDeletedFalse(String text);

    List<Answer> findByQuestionAndDeletedFalse(Question questionToAssociate);


}
