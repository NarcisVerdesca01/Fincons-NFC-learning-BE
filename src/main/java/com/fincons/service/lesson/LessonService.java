package com.fincons.service.lesson;

import com.fincons.controller.AuthController;
import com.fincons.dto.LessonDto;
import com.fincons.entity.AbilityUser;
import com.fincons.entity.Content;
import com.fincons.entity.CourseLesson;
import com.fincons.entity.Lesson;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.ContentMapper;
import com.fincons.mapper.LessonMapper;
import com.fincons.repository.ContentRepository;
import com.fincons.repository.CourseLessonRepository;
import com.fincons.repository.LessonRepository;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
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

    @Autowired
    private CourseLessonRepository courseLessonRepository;

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);


    @Override
    public List<Lesson> findAllLessons() {
        return lessonRepository.findAllByDeletedFalse();
    }

    @Override
    public Lesson findLessonById(long id) {
        if (!lessonRepository.existsByIdAndDeletedFalse(id)) {
            throw new ResourceNotFoundException("The lesson does not exist!");
        }
        return lessonRepository.findByIdAndDeletedFalse(id);
    }

    @Override
    public Lesson createLesson(LessonDto lessonDto) throws DuplicateException {

        if(StringUtils.isBlank(lessonDto.getTitle())){
            throw new IllegalArgumentException("Title required");
        }

        if (lessonRepository.existsByTitleAndDeletedFalse(lessonDto.getTitle())) {
            throw new DuplicateException("The name of lesson already exists");
        }

        Lesson lesson = new Lesson();
        if(lessonDto.getTitle() != null){
            lesson.setTitle(lessonDto.getTitle());
        }
        if(lessonDto.getBackgroundImage() != null ){
            lesson.setBackgroundImage(lessonDto.getBackgroundImage());
        }

        return lessonRepository.save(lesson);
    }

    @Override
    public Lesson updateLesson(long id, LessonDto lessonDto) throws DuplicateException {

        Lesson lessonToModify = lessonRepository.findByIdAndDeletedFalse(id);

        if(lessonToModify == null){
            throw new ResourceNotFoundException("Lesson does not exist");
        }

        if(lessonDto.getTitle() != null){
            if(!lessonRepository.existsByTitleAndIdNot(lessonDto.getTitle(),lessonToModify.getId())){
                lessonToModify.setTitle(lessonDto.getTitle());
                lessonToModify.setBackgroundImage(lessonDto.getBackgroundImage());
            }else{
                throw new DuplicateException("Lesson already exists");
            }
        }else{
            throw new IllegalArgumentException("Lesson title cannot be null");

        }
        return lessonRepository.save(lessonToModify);
    }

    @Override
    public void deleteLesson(long id)  {

        if (!lessonRepository.existsByIdAndDeletedFalse(id)) {
            throw new ResourceNotFoundException("The Lesson does not exist");
        }

        List<CourseLesson> courseLessonAssociationsToDelete = courseLessonRepository.findAllByDeletedFalse()
                .stream()
                .filter(cl-> cl.getLesson().getId()==id)
                .toList();

        courseLessonAssociationsToDelete
                .forEach(cl -> cl.setDeleted(true));
        courseLessonAssociationsToDelete
                .forEach(cl -> courseLessonRepository.save(cl));


        Lesson lessonToDelete = lessonRepository.findByIdAndDeletedFalse(id);
        lessonToDelete.setDeleted(true);
        lessonRepository.save(lessonToDelete);
        LOG.info("{} successfully deleted lesson  ' {} ' , When: {} ", SecurityContextHolder.getContext().getAuthentication().getName(), lessonToDelete.getTitle(), LocalDateTime.now());

    }


    @Override
    public Lesson associateContentToLesson(long lessonId, long contentId) throws DuplicateException, SQLIntegrityConstraintViolationException {

        Lesson existingLesson = lessonRepository.findByIdAndDeletedFalse(lessonId);

        Content existingContent = contentRepository.findByIdAndDeletedFalse(contentId);

        if(existingLesson == null){
            throw new ResourceNotFoundException("Lesson does not exist");
        }
        if(existingContent == null){
            throw new ResourceNotFoundException("Content does not exist");
        }
        if(existingLesson.getContent() != null && existingLesson.getContent().getId() == contentId){
            throw new DuplicateException("The content has already been associated with this Lesson "+ existingLesson.getContent()+ "'.");
        }

        Content content = contentRepository.findByIdAndDeletedFalse(contentId);

        if (lessonRepository.existsByContent(content)) {
            throw new SQLIntegrityConstraintViolationException("The content has already been associated with another Lesson");
        }

        existingLesson.setContent(existingContent);
        existingContent.setLesson(existingLesson);

        Lesson updatedLesson = lessonRepository.save(existingLesson);
        Content updatedContent = contentRepository.save(existingContent);
        LOG.info("{} successfully associate content: ' {} '  - lesson  ' {} ' , When: {} ", SecurityContextHolder.getContext().getAuthentication().getName(), updatedContent.getContent(),updatedLesson.getTitle(), LocalDateTime.now());
        return updatedLesson;
    }


}
