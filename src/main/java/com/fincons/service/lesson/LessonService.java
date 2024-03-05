package com.fincons.service.lesson;

import com.fincons.dto.LessonDto;
import com.fincons.entity.Content;
import com.fincons.entity.Lesson;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.ContentMapper;
import com.fincons.mapper.LessonMapper;
import com.fincons.repository.ContentRepository;
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

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private ContentRepository contentRepository;

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
    public Lesson createLesson(LessonDto lessonDto) {

        if(StringUtils.isBlank(lessonDto.getTitle())){
            throw new IllegalArgumentException("Title required");
        }

        Lesson lesson = new Lesson();
        lesson.setTitle(lessonDto.getTitle());

        return lessonRepository.save(lesson);
    }

    @Override
    public Lesson updateLesson(long id, LessonDto lessonDto) throws DuplicateException {

        if(!lessonRepository.existsById(id)){
            throw new ResourceNotFoundException("Lesson does not exist");
        }
        if(lessonRepository.existsByTitleIgnoreCase(lessonDto.getTitle())){
            throw  new DuplicateException("Title of lesson already exists!");
        }
        Lesson lessonToModify = lessonRepository.findById(id).orElseThrow(() ->  new ResourceNotFoundException("Lesson does not exist"));

       lessonToModify.setTitle(lessonDto.getTitle());
        return lessonRepository.save(lessonToModify);
    }

    @Override
    public void deleteLesson(long id)  {
        if(!lessonRepository.existsById(id)){
            throw new ResourceNotFoundException("Lesson does not exists!");
        }
        lessonRepository.deleteById(id);
    }

    @Override
    public Lesson associateContentToLesson(long lessonId, long contentId) {

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson does not exist!"));

        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ResourceNotFoundException("Content does not exist!"));

        lesson.setContent(content);
        content.setLesson(lesson);

        Lesson updatedLesson = lessonRepository.save(lesson);

        return updatedLesson;
    }


}
