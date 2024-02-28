package com.fincons.service.lesson;

import com.fincons.entity.Course;
import com.fincons.entity.Lesson;
import com.fincons.repository.LessonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LessonService implements ILessonService{
    private LessonRepository lessonRepository;

    @Override
    public List<Lesson> findAllLessons() {
        return lessonRepository.findAll();
    }



}
