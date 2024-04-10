package com.fincons.viewaudit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AuditCombinatedService implements IAuditCombinatedService{

    @Autowired
    private MyAuditCombinatedRepository myAuditCombinatedRepository;

    @Override
    public List<AuditCombinated> findAllAuditCombinated() {
        return myAuditCombinatedRepository.findAll();
    }

    @Override
    public Long[] findAllAuditCombinatedCourseInformation() {

        Long auditForCourseCreateCount = myAuditCombinatedRepository.findAll()
                .stream()
                .filter(a -> a.getCreateDate() != null && Objects.equals(a.getType(), "course"))
                .count();

        Long auditForCourseModifiedCount = myAuditCombinatedRepository.findAll()
                .stream()
                .filter(a -> a.getLastModified() != null && Objects.equals(a.getType(), "course"))
                .count();

        Long[] dataForAuditCourse = {auditForCourseCreateCount, auditForCourseModifiedCount};

        return dataForAuditCourse;
    }

    @Override
    public Long[] findAllAuditCombinatedQuizInformation() {
        Long auditForQuizCreateCount = myAuditCombinatedRepository.findAll()
                .stream()
                .filter(a -> a.getCreateDate() != null && Objects.equals(a.getType(), "quiz"))
                .count();

        Long auditForQuizModifiedCount = myAuditCombinatedRepository.findAll()
                .stream()
                .filter(a -> a.getLastModified() != null && Objects.equals(a.getType(), "quiz"))
                .count();


        Long[] dataForAuditQuiz = {auditForQuizCreateCount, auditForQuizModifiedCount};

        return dataForAuditQuiz;
    }


}
