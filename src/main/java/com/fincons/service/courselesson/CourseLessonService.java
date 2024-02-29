package com.fincons.service.courselesson;

import com.fincons.dto.CourseLessonDto;
import com.fincons.entity.Course;
import com.fincons.entity.CourseLesson;
import com.fincons.entity.Lesson;
import com.fincons.exception.CourseException;
import com.fincons.exception.CourseLessonException;
import com.fincons.exception.LessonException;
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
        return courseLessonRepository.findAll();
    }

    @Override
    public CourseLesson addCourseLesson(CourseLessonDto courseLessonDto) throws CourseException, LessonException, CourseLessonException {

        Course existingCourse = courseRepository.findById(courseLessonDto.getCourse().getId()).orElseThrow(()-> new CourseException("Course does not exist"));
        Lesson existingLesson = lessonRepository.findById(courseLessonDto.getLesson().getId()).orElseThrow(()-> new LessonException("Lesson does not exist"));

        if(courseLessonRepository.existsByCourseAndLesson(existingCourse,existingLesson)){
            throw new CourseLessonException(CourseLessonException.duplicateException());
        }
        CourseLesson courseLessonToSave = new CourseLesson(existingCourse,existingLesson);
        return courseLessonRepository.save(courseLessonToSave);
    }

    @Override
    public CourseLesson updateCourseLesson(long id, CourseLessonDto courseLessonDto) throws CourseLessonException, CourseException, LessonException {

        CourseLesson existingCourseLesson = courseLessonRepository.findById(id)
                .orElseThrow(()-> new CourseLessonException("Course-Lesson does not exist"));

        Course existingCourseToAssociate = courseRepository.findById(courseLessonDto.getCourse().getId()).orElseThrow(()-> new CourseException("Course does not exist"));
        Lesson existingLessonToAddAssociate = lessonRepository.findById(courseLessonDto.getLesson().getId()).orElseThrow(()-> new LessonException("Lesson does not exist"));

        if(courseLessonRepository.existsByCourseAndLesson(existingCourseToAssociate,existingLessonToAddAssociate)){
            throw new CourseLessonException(CourseLessonException.duplicateException());
        }

        existingCourseLesson.setCourse(existingCourseToAssociate);
        existingCourseLesson.setLesson(existingLessonToAddAssociate);

        return courseLessonRepository.save(existingCourseLesson);
    }

    @Override
    public void deleteCourseLesson(long id) throws CourseLessonException {

        if (!courseLessonRepository.existsById(id)) {
            throw new CourseLessonException("The course-lesson does not exist") ;
        }

        courseLessonRepository.deleteById(id);
    }

    @Override
    public CourseLesson getCourseLessonById(long id) {
        return courseLessonRepository
                .findById(id).orElseThrow(()-> new ResourceNotFoundException("The Course-Lesson association does not exist"));
    }


}
