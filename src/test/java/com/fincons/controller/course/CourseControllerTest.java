package com.fincons.controller.course;


import com.fincons.controller.CourseController;
import com.fincons.dto.AbilityDto;
import com.fincons.dto.ContentDto;
import com.fincons.dto.CourseDto;
import com.fincons.entity.Ability;
import com.fincons.entity.Content;
import com.fincons.entity.Course;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
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
import static org.mockito.Mockito.doThrow;
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


    @Test
    public void testCreateCourse_Duplicate() throws DuplicateException {
        CourseDto inputCourseDto = new CourseDto();
        when(iCourseService.createCourse(inputCourseDto)).thenThrow(new DuplicateException("The name of course already exist"));
        ResponseEntity<ApiResponse<CourseDto>> responseEntity = courseController.createCourse(inputCourseDto);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("The name of course already exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    public void testGetCourseById_Success(){
        Course course = new Course(1L, "randomNameOfCourse", "randomImage", "randmDescription", null, null,null,null,null,null,"randomImage");
        when(iCourseService.findCourseById(1L)).thenReturn(course);
        ResponseEntity<ApiResponse<CourseDto>> responseEntity = courseController.getCourseById(courseMapper.mapCourseToCourseDto(course).getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        CourseDto courseDto = Objects.requireNonNull(responseEntity.getBody()).getData();
        assertNotNull(courseDto);
        assertEquals(1, courseDto.getId());
        assertEquals(course.getId(), courseDto.getId());
        assertEquals(course.getDescription(), courseDto.getDescription());
    }

    @Test
    public void testGetCourseById_NotFound(){
            long nonExistingCoursetId = 1L;
            doThrow(new ResourceNotFoundException("The course does not exist")).when(iCourseService).findCourseById(nonExistingCoursetId);
            ResponseEntity<ApiResponse<CourseDto>> responseEntity = courseController.getCourseById(nonExistingCoursetId);
            assertEquals("The course does not exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
            assertEquals(HttpStatus.NOT_FOUND,responseEntity.getStatusCode());
    }

    @Test
    public void testGetCourseByName_Success(){
        Course course = new Course(1L, "randomNameOfCourse", "randomImage", "randmDescription", null, null,null,null,null,null,"randomImage");
        when(iCourseService.findCourseByName("randomNameOfCourse")).thenReturn(course);
        ResponseEntity<ApiResponse<CourseDto>> responseEntity = courseController.getCourseByName("randomNameOfCourse");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        CourseDto courseDto = Objects.requireNonNull(responseEntity.getBody()).getData();
        assertNotNull(courseDto);
        assertEquals(1, courseDto.getId());
        assertEquals(course.getId(), courseDto.getId());
        assertEquals(course.getName(), courseDto.getName());
    }

    @Test
    public void testGetCourseByName_NotFound(){
        Course course = new Course(1L, "randomNameOfCourse", "randomImage", "randmDescription", null, null,null,null,null,null,"randomImage");
        String nonExistingCourseName = "RandomNonExistingName";
        doThrow(new ResourceNotFoundException("The Course does not exist")).when(iCourseService).findCourseByName(nonExistingCourseName);
        ResponseEntity<ApiResponse<CourseDto>> responseEntity = courseController.getCourseByName(nonExistingCourseName);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("The Course does not exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    public void testDeleteCourse_Success() {
        long courseId = 1L;
        ResponseEntity<ApiResponse<String>> responseEntity = courseController.deleteCourse(courseId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("The course has been successfully deleted!", Objects.requireNonNull(responseEntity.getBody()).getData());
        verify(iCourseService).deleteCourse(courseId);
    }

    @Test
    public void testDeleteCourse_ResourceNotFound() {
        long nonExistingAbilityId = 999L;
        doThrow(new ResourceNotFoundException("The course does not exist")).when(iCourseService).deleteCourse(nonExistingAbilityId);
        ResponseEntity<ApiResponse<String>> responseEntity = courseController.deleteCourse(nonExistingAbilityId);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("The course does not exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }




}