package com.fincons.service.lesson;

import com.fincons.dto.LessonDto;
import com.fincons.entity.Lesson;
import com.fincons.exception.CourseException;
import com.fincons.exception.LessonException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.ContentMapper;
import com.fincons.mapper.LessonMapper;
import com.fincons.repository.LessonRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonService implements ILessonService{

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private LessonMapper lessonMapper;

    @Autowired
    private ContentMapper contentMapper;

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

    @Override
    public Lesson createLesson(LessonDto lessonDto) throws LessonException {

        if(StringUtils.isBlank(lessonDto.getTitle())){
            throw new IllegalArgumentException("Title required");
        }

        Lesson lesson = new Lesson();
        lesson.setTitle(lessonDto.getTitle());

        return lessonRepository.save(lesson);
    }

    @Override
    public Lesson updateLesson(long id, LessonDto lessonDto) throws LessonException {

        if(!lessonRepository.existsById(id)){
            throw new LessonException("Lesson does not exist");
        }
        if(lessonRepository.existsByTitleIgnoreCase(lessonDto.getTitle())){
            throw  new LessonException("Title of lesson already exists!");
        }
        Lesson lessonToModify = lessonRepository.findById(id).orElseThrow(() ->  new LessonException("Lesson does not exist"));

       lessonToModify.setTitle(lessonDto.getTitle());
        return lessonRepository.save(lessonToModify);
    }

    @Override
    public void deleteLesson(long id) throws LessonException {
        if(!lessonRepository.existsById(id)){
            throw new LessonException("Lesson does not exists!");
        }
        lessonRepository.deleteById(id);
    }

    //TODO associare contenuto a lezione;


}
