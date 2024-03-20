package com.fincons.controller.course;

import com.fincons.controller.CourseController;
import com.fincons.dto.CourseDto;
import com.fincons.entity.Course;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.exception.UserDataException;
import com.fincons.jwt.JwtTokenProvider;
import com.fincons.mapper.CourseMapper;
import com.fincons.service.course.ICourseService;
import com.fincons.utility.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
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
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

     //ctrl-shift-7
    @Test
    public void testGetAllCourses_Success(){
        Course course1 = new Course(1L, "randomNameOfCourse", "randomImage", "randmDescription", null, null,"randomSecondImage",false,null,null,null,null);
        Course course2 = new Course(2L, "randomNameOfCourse2", "randomImage2", "randmDescription2", null, null,"random second image",false,null,null,null,null);
        Course course3 = new Course(3L, "randomNameOfCourse", "randomImage", "randmDescription", null, null,"randomSecondImage",true,null,null,null,null);

        List<Course> courseList = Arrays.asList(course1,course2,course3);
        when(iCourseService.findAllCourses()).thenReturn(courseList
                .stream()
                .filter(c->!c.isDeleted())
                .toList());

        ResponseEntity<ApiResponse<List<CourseDto>>> responseEntity = courseController.getAllCourses();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<CourseDto> responseCourses = Objects.requireNonNull(responseEntity.getBody()).getData();
        assertNotNull(responseCourses);
        assertEquals(2, responseCourses.size());
        assertEquals("randomNameOfCourse", responseCourses.get(0).getName());
        assertEquals("randomNameOfCourse2",responseCourses.get(1).getName());
        assertFalse( responseCourses.get(0).isDeleted());
        assertFalse(responseCourses.get(1).isDeleted());
    }

    @Test
    public void testCreateCourse_Success() throws DuplicateException {
        CourseDto inputCourseDto = new CourseDto(1L, "randomNameOfCourse", "randomImage", "randmDescription", null, null,null,false,null,null,null,"randomImage");
        when(iCourseService.createCourse(inputCourseDto)).thenReturn(courseMapper.mapDtoToCourse(inputCourseDto));
        ResponseEntity<ApiResponse<CourseDto>> responseEntity = courseController.createCourse(inputCourseDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(inputCourseDto.getName(), Objects.requireNonNull(responseEntity.getBody()).getData().getName());
        verify(iCourseService).createCourse(inputCourseDto);
        assertFalse(responseEntity.getBody().getData().isDeleted());
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
        Course course = new Course(1L, "randomNameOfCourse", "randomImage", "randmDescription", null, null,null,false,null,null,null,"randomImage");
        when(iCourseService.findCourseById(1L)).thenReturn(course);
        ResponseEntity<ApiResponse<CourseDto>> responseEntity = courseController.getCourseById(courseMapper.mapCourseToCourseDto(course).getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        CourseDto courseDto = Objects.requireNonNull(responseEntity.getBody()).getData();
        assertNotNull(courseDto);
        assertEquals(1, courseDto.getId());
        assertEquals(course.getId(), courseDto.getId());
        assertEquals(course.getDescription(), courseDto.getDescription());
        assertFalse(responseEntity.getBody().getData().isDeleted());
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
        Course course = new Course(1L, "randomNameOfCourse", "randomImage", "randmDescription", null, null,null,false,null,null,null,"randomImage");
        when(iCourseService.findCourseByName("randomNameOfCourse")).thenReturn(course);
        ResponseEntity<ApiResponse<CourseDto>> responseEntity = courseController.getCourseByName("randomNameOfCourse");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        CourseDto courseDto = Objects.requireNonNull(responseEntity.getBody()).getData();
        assertNotNull(courseDto);
        assertEquals(1, courseDto.getId());
        assertEquals(course.getId(), courseDto.getId());
        assertEquals(course.getName(), courseDto.getName());
        assertFalse(responseEntity.getBody().getData().isDeleted());
    }

    @Test
    public void testGetCourseByName_NotFound(){
        Course course = new Course(1L, "randomNameOfCourse", "randomImage", "randmDescription", null, null,null,false,null,null,null,"randomImage");
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

    @Test
    public void testGetDedicatedCourses_Success() throws UserDataException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer mock_token");
        when(jwtTokenProvider.getEmailFromJWT("mock_token")).thenReturn("test@example.com");
        List<Course> courses = Arrays.asList(
                new Course(1L, "Course 1", "image1", "Description 1", null, null, null,false, null, null, null, null),
                new Course(2L, "Course 2", "image2", "Description 2", null, null, null,false, null, null, null, null)
        );
        when(iCourseService.findDedicatedCourses()).thenReturn(courses);
        ResponseEntity<ApiResponse<List<CourseDto>>> responseEntity = courseController.getDedicatedCourses();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<CourseDto> responseCourses = Objects.requireNonNull(responseEntity.getBody()).getData();
        assertNotNull(responseCourses);
        assertEquals(2, responseCourses.size());
        assertEquals("Course 1", responseCourses.get(0).getName());
        assertEquals("Course 2", responseCourses.get(1).getName());
    }

    @Test
    public void testGetDedicatedCourses_ResourceNotFoundException() throws UserDataException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer mock_token");
        when(jwtTokenProvider.getEmailFromJWT("mock_token")).thenReturn("test@example.com");
        doThrow(new ResourceNotFoundException("The User does not exist") ).when(iCourseService).findDedicatedCourses();
        ResponseEntity<ApiResponse<List<CourseDto>>> responseEntity = courseController.getDedicatedCourses();
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        String errorMessage = Objects.requireNonNull(responseEntity.getBody()).getMessage();
        assertNotNull(errorMessage);
        assertEquals("The User does not exist", errorMessage);
    }

    @Test
    public void testUpdateCourse_Success() throws ResourceNotFoundException, DuplicateException {
        long courseId = 1L;
        CourseDto inputCourseDto = new CourseDto(1L, "Updated Course", "updatedImage", "Updated Description", null, null, null,false, null, null, null, "updatedBackgroundImage");
        Course updatedCourse = new Course(1L, "Updated Course", "updatedImage", "Updated Description", null, null, null, false, null, null, null, "updatedBackgroundImage");
        when(iCourseService.updateCourse(courseId, inputCourseDto)).thenReturn(updatedCourse);
        ResponseEntity<ApiResponse<CourseDto>> responseEntity = courseController.updateCourse(courseId, inputCourseDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        CourseDto updatedCourseDto = Objects.requireNonNull(responseEntity.getBody()).getData();
        assertNotNull(updatedCourseDto);
        assertEquals(updatedCourse.getName(), responseEntity.getBody().getData().getName());
    }

    @Test
    public void testUpdateCourse_ResourceNotFound() throws ResourceNotFoundException, DuplicateException {
        long courseId = 1L;
        CourseDto inputCourseDto = new CourseDto(1L, "Updated Course", "updatedImage", "Updated Description", null, null, null, false, null, null, null, "updatedBackgroundImage");
        Course updatedCourse = new Course(1L, "Updated Course", "updatedImage", "Updated Description", null, null, null, false, null, null, null, "updatedBackgroundImage");
        when(iCourseService.updateCourse(courseId, inputCourseDto)).thenThrow(new ResourceNotFoundException("The Course does not exists"));
        ResponseEntity<ApiResponse<CourseDto>> responseEntity = courseController.updateCourse(courseId, inputCourseDto);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("The Course does not exists", responseEntity.getBody().getMessage());
    }

    @Test
    public void testCreateAbility_Duplicate() throws DuplicateException {
        CourseDto inputCourseDto = new CourseDto();
        when(iCourseService.createCourse(inputCourseDto)).thenThrow(new DuplicateException("The name of course already exists"));
        ResponseEntity<ApiResponse<CourseDto>> responseEntity = courseController.createCourse(inputCourseDto);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("The name of course already exists", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }


}