package com.fincons.service.lesson;

import com.fincons.dto.LessonDto;
import com.fincons.entity.Lesson;
import com.fincons.exception.CourseException;
import com.fincons.exception.LessonException;
import com.fincons.exception.ResourceNotFoundException;
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
            throw new LessonException("Title required");
        }
        if(lessonRepository.existsByTitle(lessonDto.getTitle())){
            throw new LessonException("Lesson with this title already exists");
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
        if(lessonRepository.existsByTitle(lessonDto.getTitle())){
            throw  new LessonException("Title of lesson already exists!");
        }

        Lesson lessonToModify = lessonRepository.findById(id).orElseThrow(() ->  new LessonException("Lesson does not exist"));

        if(!lessonRepository.existsByTitle(lessonDto.getTitle())){
            lessonToModify.setTitle(lessonDto.getTitle());
        }else if(lessonRepository.existsByTitle(lessonDto.getTitle())){
            throw new LessonException("Title of lesson already exists");
        }

        return lessonRepository.save(lessonToModify);
    }


}
