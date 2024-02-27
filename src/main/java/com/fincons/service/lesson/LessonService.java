package com.fincons.service.lesson;

import com.fincons.dto.LessonDto;
import com.fincons.entity.Course;
import com.fincons.entity.Lesson;
import com.fincons.exception.CourseException;
import com.fincons.mapper.LessonMapper;
import com.fincons.repository.LessonRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LessonService implements ILessonService{
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private LessonMapper lessonMapper;

    @Override
    public List<Lesson> findAllCourses() {
        return lessonRepository.findAll();
    }

    @Override
    public Lesson createLesson(LessonDto lessonDto) throws CourseException {
       /*
        if(StringUtils.isBlank(lessonDto.getTitle()) || lessonDto.getCourses().size()==0 || lessonDto.getQuiz()==null || lessonDto.getContent()== null || lessonDto.getCreateDate()==null || lessonDto.getLastModified()==null || lessonDto.getCreatedBy()== null){
            throw new CourseException("Title, content or requirements not present");
        } */
        if(lessonRepository.existsByTitle(lessonDto.getTitle())){
            throw new CourseException("The name of course already exist");
        }
        Lesson lesson = new Lesson(lessonMapper.mapLessonDtoToLessonEntity(lessonDto));
        Lesson savedLesson= lessonRepository.save(lesson);
        return savedLesson;
    }


}
