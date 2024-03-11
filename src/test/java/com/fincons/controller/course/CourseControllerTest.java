package com.fincons.controller.course;


import com.fincons.controller.CourseController;
import com.fincons.dto.AbilityDto;
import com.fincons.dto.CourseDto;
import com.fincons.entity.Course;
import com.fincons.exception.DuplicateException;
import com.fincons.mapper.CourseMapper;
import com.fincons.service.course.ICourseService;
import com.fincons.utility.ApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class CourseControllerTest {


    @Autowired
    private CourseController courseController;

    @MockBean
    private ICourseService iCourseService;

    @Autowired
    private CourseMapper courseMapper;


     //ctrl-shift-7
    @Test
    public void testGetAllCourses_Success(){
        Course course1 = new Course(1L, "randomNameOfCourse", "randomImage", "randmDescription", null, null,"randomSecondImage",null,null,null,null);
        Course course2 = new Course(2L, "randomNameOfCourse2", "randomImage2", "randmDescription2", null, null,"random second image",null,null,null,null);
        List<Course> courseList = Arrays.asList(course1, course2);
        when(iCourseService.findAllCourses()).thenReturn(courseList);
        List<CourseDto> courseDtoList = courseList.stream().map(c -> courseMapper.mapCourseToCourseDto(c)).toList();
        ResponseEntity<ApiResponse<List<CourseDto>>> responseEntity = courseController.getAllCourses();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<CourseDto> responseCourses = Objects.requireNonNull(responseEntity.getBody()).getData();
        assertNotNull(responseCourses);
        assertEquals(2, responseCourses.size());
        assertEquals("randomNameOfCourse", responseCourses.get(0).getName());
        assertEquals("randomNameOfCourse2",responseCourses.get(1).getName());
    }

    @Test
    public void testCreateCourse_Success() throws DuplicateException {
        CourseDto inputCourseDto = new CourseDto(1L, "randomNameOfCourse", "randomImage", "randmDescription", null, null,null,null,null,null,"randomImage");
        when(iCourseService.createCourse(inputCourseDto)).thenReturn(courseMapper.mapDtoToCourse(inputCourseDto));
        ResponseEntity<ApiResponse<CourseDto>> responseEntity = courseController.createCourse(inputCourseDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(inputCourseDto.getName(), Objects.requireNonNull(responseEntity.getBody()).getData().getName());
        verify(iCourseService).createCourse(inputCourseDto);
    }

    @Test
    public void testCreateCourse_InvalidInput() throws DuplicateException {
        CourseDto invalidCourseDto = new CourseDto();
        when(iCourseService.createCourse(invalidCourseDto)).thenThrow(new IllegalArgumentException("Name, description or background image not present"));
        ResponseEntity<ApiResponse<CourseDto>> responseEntity = courseController.createCourse(invalidCourseDto);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Name, description or background image not present", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }













}