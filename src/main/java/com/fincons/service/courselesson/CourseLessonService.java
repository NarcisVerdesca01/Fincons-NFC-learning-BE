package com.fincons.service.courselesson;

import com.fincons.dto.CourseLessonDto;
import com.fincons.entity.Course;
import com.fincons.entity.CourseLesson;
import com.fincons.entity.Lesson;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.repository.CourseLessonRepository;
import com.fincons.repository.CourseRepository;
import com.fincons.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CourseLessonService implements ICourseLessonService {

    @Autowired
    private CourseLessonRepository courseLessonRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private LessonRepository lessonRepository;


    @Override
    public List<CourseLesson> getCourseLessonList() {
        return courseLessonRepository.findAllByDeletedFalse();
    }

    @Override
    public CourseLesson addCourseLesson(CourseLessonDto courseLessonDto) throws DuplicateException {

        Course existingCourse = courseRepository.findByIdAndDeletedFalse(courseLessonDto.getCourse().getId());
        Lesson existingLesson = lessonRepository.findByIdAndDeletedFalse(courseLessonDto.getLesson().getId());

        if(existingCourse == null){
            throw new ResourceNotFoundException("Course does not exist");
        }
        if(existingLesson == null){
            throw new ResourceNotFoundException("Lesson does not exist");
        }

        if(courseLessonRepository.existsByCourseAndLessonAndDeletedFalse(existingCourse,existingLesson)){
            throw new DuplicateException("Course-Lesson association already exists");
        }

        CourseLesson courseLessonToSave = new CourseLesson(existingCourse,existingLesson);
        return courseLessonRepository.save(courseLessonToSave);
    }

    @Override
    public CourseLesson updateCourseLesson(long id, CourseLessonDto courseLessonDto) throws  DuplicateException {

        CourseLesson existingCourseLesson = courseLessonRepository.findByIdAndDeletedFalse(id);
        if(existingCourseLesson == null){
            throw new ResourceNotFoundException("Course-Lesson association does not exist");
        }


        Course existingCourseToAssociate = courseRepository.findByIdAndDeletedFalse(courseLessonDto.getCourse().getId());
        Lesson existingLessonToAddAssociate = lessonRepository.findByIdAndDeletedFalse(courseLessonDto.getLesson().getId());

        if(courseLessonRepository.existsByCourseAndLessonAndDeletedFalse(existingCourseToAssociate,existingLessonToAddAssociate)){
            throw new DuplicateException("Course-Lesson association already exists");
        }

        existingCourseLesson.setCourse(existingCourseToAssociate);
        existingCourseLesson.setLesson(existingLessonToAddAssociate);

        return courseLessonRepository.save(existingCourseLesson);
    }

    @Override
    public void deleteCourseLesson(long id)  {

        if (!courseLessonRepository.existsByIdAndDeletedFalse(id)) {
            throw new ResourceNotFoundException("The course-lesson does not exist") ;
        }
        CourseLesson courseLessonToDelete = courseLessonRepository.findByIdAndDeletedFalse(id);
        courseLessonToDelete.setDeleted(true);
        courseLessonRepository.save(courseLessonToDelete);
    }

    @Override
    public CourseLesson getCourseLessonById(long id) {
        if(!courseLessonRepository.existsByIdAndDeletedFalse(id)){
            throw new ResourceNotFoundException("The Course-Lesson association does not exist!");

        }
          return courseLessonRepository.findByIdAndDeletedFalse(id);
    }


}
