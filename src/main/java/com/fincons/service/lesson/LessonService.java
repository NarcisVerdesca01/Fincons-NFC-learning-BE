package com.fincons.service.lesson;

import com.fincons.entity.Lesson;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LessonService implements ILessonService{

    @Autowired
    private LessonRepository lessonRepository;

    @Override
    public List<Lesson> findAllLessons() {
        return lessonRepository.findAll();
    }

    @Override
    public Lesson findLessonById(long id) {
        if (!lessonRepository.existsById(id)) {
            throw new ResourceNotFoundException("The lesson does not exist!");
        }
        return lessonRepository.findById(id).orElseThrow(null);
    }


}
