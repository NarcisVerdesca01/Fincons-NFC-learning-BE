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
        return courseLessonRepository.findAll();
    }

    @Override
    public CourseLesson addCourseLesson(CourseLessonDto courseLessonDto) throws DuplicateException {

        Course existingCourse = courseRepository.findById(courseLessonDto.getCourse().getId()).orElseThrow(()-> new ResourceNotFoundException("Course does not exist"));
        Lesson existingLesson = lessonRepository.findById(courseLessonDto.getLesson().getId()).orElseThrow(()-> new ResourceNotFoundException("Lesson does not exist"));

        if(courseLessonRepository.existsByCourseAndLesson(existingCourse,existingLesson)){
            throw new DuplicateException("Course-Lesson association already exists");
        }
        CourseLesson courseLessonToSave = new CourseLesson(existingCourse,existingLesson);
        return courseLessonRepository.save(courseLessonToSave);
    }

    @Override
    public CourseLesson updateCourseLesson(long id, CourseLessonDto courseLessonDto) throws  DuplicateException {

        CourseLesson existingCourseLesson = courseLessonRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Course-Lesson does not exist"));

        Course existingCourseToAssociate = courseRepository.findById(courseLessonDto.getCourse().getId()).orElseThrow(()-> new ResourceNotFoundException("Course does not exist"));
        Lesson existingLessonToAddAssociate = lessonRepository.findById(courseLessonDto.getLesson().getId()).orElseThrow(()-> new ResourceNotFoundException("Lesson does not exist"));

        if(courseLessonRepository.existsByCourseAndLesson(existingCourseToAssociate,existingLessonToAddAssociate)){
            throw new DuplicateException("Course-Lesson association already exists");
        }

        existingCourseLesson.setCourse(existingCourseToAssociate);
        existingCourseLesson.setLesson(existingLessonToAddAssociate);

        return courseLessonRepository.save(existingCourseLesson);
    }

    @Override
    public void deleteCourseLesson(long id)  {

        if (!courseLessonRepository.existsById(id)) {
            throw new ResourceNotFoundException("The course-lesson does not exist") ;
        }

        courseLessonRepository.deleteById(id);
    }

    @Override
    public CourseLesson getCourseLessonById(long id) {
        return courseLessonRepository
                .findById(id).orElseThrow(()-> new ResourceNotFoundException("The Course-Lesson association does not exist"));
    }


}
