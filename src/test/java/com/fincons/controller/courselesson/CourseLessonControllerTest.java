package com.fincons.controller.courselesson;

import com.fincons.controller.CourseLessonController;
import com.fincons.dto.CourseDto;
import com.fincons.dto.CourseLessonDto;
import com.fincons.dto.LessonDto;
import com.fincons.entity.Course;
import com.fincons.entity.CourseLesson;
import com.fincons.entity.Lesson;
import com.fincons.entity.Role;
import com.fincons.entity.User;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.jwt.JwtTokenProvider;
import com.fincons.mapper.CourseLessonMapper;
import com.fincons.service.authorization.AuthService;
import com.fincons.service.courselesson.ICourseLessonService;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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
 class CourseLessonControllerTest {

    @Autowired
    private CourseLessonController courseLessonController;

    @MockBean
    private ICourseLessonService iCourseLessonService;

    @MockBean
    private AuthService authService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CourseLessonMapper courseLessonMapper;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;



    @Test
    void testGetAllCourseLesson_Success(){
        List<CourseLesson> courseLessonList = getCourseLessonListData();
        when(iCourseLessonService.getCourseLessonList()).thenReturn(courseLessonList
                .stream()
                .filter(ac->!ac.isDeleted())
                .toList());
        ResponseEntity<ApiResponse<List<CourseLessonDto>>> responseEntity = courseLessonController.getAllCourseLesson();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(Objects.requireNonNull(responseEntity.getBody()).getData());
        assertEquals(2, responseEntity.getBody().getData().size());
        assertFalse( responseEntity.getBody().getData().get(0).isDeleted());
        assertFalse( responseEntity.getBody().getData().get(1).isDeleted());
    }

    @Test
    void testCreateCourseLesson_Success() throws DuplicateException{
        User tutor = getTutor("tutor@gmail.com", "ROLE_TUTOR");
        HttpServletRequest request = getHttpServletRequest(tutor);
        when(request.getHeader("Authorization")).thenReturn("Bearer mock_token");
        when(jwtTokenProvider.getEmailFromJWT("mock_token")).thenReturn(tutor.getEmail());
        CourseDto courseDto = new CourseDto(
                1L, "randomNameOfCourse", "randomImage", "randmDescription", null, null,
                null,false,null,null,null,"randomImage");
        LessonDto lessonDto = new LessonDto(
                1L,"Lesson 3: Type of data!",null,null,null,"random backgroundimage",
                false,null,null,null,null
        );
        CourseLessonDto inputCourseLessonAssociationByUser= new CourseLessonDto(1L,courseDto,lessonDto,false);
        when(iCourseLessonService.addCourseLesson(inputCourseLessonAssociationByUser)).thenReturn(courseLessonMapper.mapCourseLessonDtoToEntity(inputCourseLessonAssociationByUser));
        ResponseEntity<ApiResponse<CourseLessonDto>> responseEntity = courseLessonController.addCourseLesson(inputCourseLessonAssociationByUser);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(inputCourseLessonAssociationByUser.getLesson().getId(), Objects.requireNonNull(responseEntity.getBody()).getData().getLesson().getId());
        assertEquals(inputCourseLessonAssociationByUser.getCourse().getId(), Objects.requireNonNull(responseEntity.getBody()).getData().getCourse().getId());
        verify(iCourseLessonService).addCourseLesson(inputCourseLessonAssociationByUser);
    }

    @Test
    void testCreateCourseLesson_ResourceNotFoundException() throws DuplicateException {
        User tutor = getTutor("tutor@gmail.com", "ROLE_TUTOR");
        HttpServletRequest request = getHttpServletRequest(tutor);
        when(request.getHeader("Authorization")).thenReturn("Bearer mock_token");
        when(jwtTokenProvider.getEmailFromJWT("mock_token")).thenReturn(tutor.getEmail());
        CourseLessonDto invalidCourseLessonDto = new CourseLessonDto();
        doThrow(new ResourceNotFoundException("The course does not exist")).when(iCourseLessonService).addCourseLesson(invalidCourseLessonDto);
        ResponseEntity<ApiResponse<CourseLessonDto>> responseEntity = courseLessonController.addCourseLesson(invalidCourseLessonDto);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("The course does not exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    void testCreateCourseLesson_DuplicateException() throws DuplicateException {
        User tutor = getTutor("tutor@gmail.com", "ROLE_TUTOR");
        HttpServletRequest request = getHttpServletRequest(tutor);
        when(request.getHeader("Authorization")).thenReturn("Bearer mock_token");
        when(jwtTokenProvider.getEmailFromJWT("mock_token")).thenReturn(tutor.getEmail());
        CourseLessonDto invalidCourseLessonDto = new CourseLessonDto();
        doThrow(new DuplicateException("The course already exist")).when(iCourseLessonService).addCourseLesson(invalidCourseLessonDto);
        ResponseEntity<ApiResponse<CourseLessonDto>> responseEntity = courseLessonController.addCourseLesson(invalidCourseLessonDto);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("The course already exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    void testUpdateCourseLesson_Success() throws DuplicateException {
        User tutor = getTutor("tutor@gmail.com", "ROLE_TUTOR");
        HttpServletRequest request = getHttpServletRequest(tutor);
        when(request.getHeader("Authorization")).thenReturn("Bearer mock_token");
        when(jwtTokenProvider.getEmailFromJWT("mock_token")).thenReturn(tutor.getEmail());
        Course course1 = new Course(
                1L, "randomNameOfCourse", "randomImage", "randmDescription", null, null,
                null,false,null,null,null,"randomImage");
        Lesson lesson1 = new Lesson(
                1L,"Lesson 3: Type of data!",null,null,null,"random backgroundimage",
                false,null,null,null,null
        );
        CourseDto courseDto = new CourseDto(
                1L, "randomNameOfCourse", "randomImage", "randmDescription", null, null,
                null,false,null,null,null,"randomImage");
        LessonDto lessonDto = new LessonDto(
                1L,"Lesson 3: Type of data!",null,null,null,"random backgroundimage",
                false,null,null,null,null
        );
        long idOfCourseLessonAssociationToModify = 1L;
        CourseLessonDto inputCourseLessonDto = new CourseLessonDto(1L, courseDto, lessonDto,false);
        CourseLesson updatdCourseLesson = new CourseLesson(1L, course1, lesson1,false);
        when(iCourseLessonService.updateCourseLesson(idOfCourseLessonAssociationToModify, inputCourseLessonDto)).thenReturn(updatdCourseLesson);
        ResponseEntity<ApiResponse<CourseLessonDto>> responseEntity = courseLessonController.updateCourseLesson(idOfCourseLessonAssociationToModify, inputCourseLessonDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatdCourseLesson.getCourse().getName(), Objects.requireNonNull(Objects.requireNonNull(responseEntity.getBody()).getData().getCourse().getName()));
        assertFalse(responseEntity.getBody().getData().isDeleted());
    }

    @Test
    void testUpdateCourseLesson_ResourceNotFound() throws DuplicateException {
        User tutor = getTutor("tutor@gmail.com", "ROLE_TUTOR");
        HttpServletRequest request = getHttpServletRequest(tutor);
        when(request.getHeader("Authorization")).thenReturn("Bearer mock_token");
        when(jwtTokenProvider.getEmailFromJWT("mock_token")).thenReturn(tutor.getEmail());
        long nonExistingCourseLessonId = 999L;
        CourseLessonDto inputCourseLessonDto = new CourseLessonDto();
        when(iCourseLessonService.updateCourseLesson(nonExistingCourseLessonId, inputCourseLessonDto))
                .thenThrow(new ResourceNotFoundException("Course-Lesson association does not exist"));
        ResponseEntity<ApiResponse<CourseLessonDto>> responseEntity = courseLessonController.updateCourseLesson(nonExistingCourseLessonId, inputCourseLessonDto);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Course-Lesson association does not exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    void testUpdateAbilityCourse_Duplicate() throws DuplicateException {
        User tutor = getTutor("tutor@gmail.com", "ROLE_TUTOR");
        HttpServletRequest request = getHttpServletRequest(tutor);
        when(request.getHeader("Authorization")).thenReturn("Bearer mock_token");
        when(jwtTokenProvider.getEmailFromJWT("mock_token")).thenReturn(tutor.getEmail());
        long courseLessonId = 1L;
        CourseLessonDto inputCourseLessonDto = new CourseLessonDto();
        when(iCourseLessonService.updateCourseLesson(courseLessonId, inputCourseLessonDto))
                .thenThrow(new DuplicateException("Course-Lesson association already exists!"));
        ResponseEntity<ApiResponse<CourseLessonDto>> responseEntity = courseLessonController.updateCourseLesson(courseLessonId, inputCourseLessonDto);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("Course-Lesson association already exists!", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    void testDeleteCourseLessonAssociation_Success() {
        User admin = getTutor("admin@gmail.com", "ROLE_ADMIN");
        HttpServletRequest request = getHttpServletRequest(admin);
        when(request.getHeader("Authorization")).thenReturn("Bearer mock_token");
        when(jwtTokenProvider.getEmailFromJWT("mock_token")).thenReturn(admin.getEmail());
        Course course1 = new Course(
                1L, "randomNameOfCourse", "randomImage", "randmDescription", null, null,
                null,false,null,null,null,"randomImage");
        Lesson lesson1 = new Lesson(
                1L,"Lesson 3: Type of data!",null,null,null,"random backgroundimage",
                false,null,null,null,null
        );

        CourseLesson courseLessonToDelete = new CourseLesson(1L, course1, lesson1,false);
        ResponseEntity<ApiResponse<String>> responseEntity = courseLessonController.deleteCourseLesson(courseLessonToDelete.getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Deleted relationship between course and lesson chosen", Objects.requireNonNull(responseEntity.getBody()).getMessage());
        verify(iCourseLessonService).deleteCourseLesson(courseLessonToDelete.getId());
    }

    @Test
    void testDeleteCourseLesson_ResourceNotFound() {
        User admin = getTutor("admin@gmail.com", "ROLE_ADMIN");
        HttpServletRequest request = getHttpServletRequest(admin);
        when(request.getHeader("Authorization")).thenReturn("Bearer mock_token");
        when(jwtTokenProvider.getEmailFromJWT("mock_token")).thenReturn(admin.getEmail());
        long nonExistingCourseLessonId = 999L;
        doThrow(new ResourceNotFoundException("The course lesson association does not exist")).when(iCourseLessonService).deleteCourseLesson(nonExistingCourseLessonId);
        ResponseEntity<ApiResponse<String>> responseEntity = courseLessonController.deleteCourseLesson(nonExistingCourseLessonId);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("The course lesson association does not exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    void testGetCourseLessonById_Success() {
        Course course1 = new Course(
                1L, "randomNameOfCourse", "randomImage", "randmDescription", null, null,
                null,false,null,null,null,"randomImage");
        Lesson lesson1 = new Lesson(
                1L,"Lesson 3: Type of data!",null,null,null,"random backgroundimage",
                false,null,null,null,null
        );
        CourseLesson courseLesson = new CourseLesson(1L, course1, lesson1,false);
        when(iCourseLessonService.getCourseLessonById(1L)).thenReturn(courseLesson);
        ResponseEntity<ApiResponse<CourseLessonDto>> responseEntity = courseLessonController.getCourseLessonById(courseLessonMapper.mapCourseLessonEntityToDto(courseLesson).getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(courseLesson.getId(), Objects.requireNonNull(responseEntity.getBody()).getData().getId());
        assertFalse(responseEntity.getBody().getData().isDeleted());

    }

    @Test
    void testGetCourseLessonById_ResourceNotFound(){
        long nonExistingCourseLessonId = 1L;
        doThrow(new ResourceNotFoundException("The course-lesson association does not exist")).when(iCourseLessonService).getCourseLessonById(nonExistingCourseLessonId);
        ResponseEntity<ApiResponse<CourseLessonDto>> responseEntity = courseLessonController.getCourseLessonById(nonExistingCourseLessonId);
        assertEquals("The course-lesson association does not exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
        assertEquals(HttpStatus.NOT_FOUND,responseEntity.getStatusCode());
    }

    private List<CourseLesson> getCourseLessonListData() {
        Course course1 = new Course(
                1L, "randomNameOfCourse", "randomImage", "randmDescription", null, null,
                null,false,null,null,null,"randomImage");
        Course course2 = new Course(
                2L, "randomNameOfCourse2", "randomImage", "randmDescription", null, null,
                null,false,null,null,null,"randomImage");
        Lesson lesson1 = new Lesson(
                1L,"Lesson 3: Type of data!",null,null,null,"random backgroundimage",
                false,null,null,null,null
        );
        Lesson lesson2 = new Lesson(
                2L,"Lesson 4:",null,null,null,"random backgroundimage",
                false,null,null,null,null
        );
        CourseLesson courseLesson1 = new CourseLesson(1L,course1,lesson1,false);
        CourseLesson courseLesson2 = new CourseLesson(2L,course2,lesson2,false);
        return java.util.Arrays.asList(courseLesson1,courseLesson2);
    }

    private static HttpServletRequest getHttpServletRequest(User tutor) {
        Authentication auth = new UsernamePasswordAuthenticationToken(tutor.getEmail(), tutor.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);
        HttpServletRequest request = mock(HttpServletRequest.class);
        return request;
    }

    private User getTutor(String mail, String ROLE_TUTOR) {
        User tutor = new User();
        tutor.setFirstName("tutor");
        tutor.setLastName("tutor");
        tutor.setEmail(mail);
        tutor.setPassword(passwordEncoder.encode("Password!"));
        Role role = new Role(1L, ROLE_TUTOR, null, false);
        tutor.setRoles(List.of(role));
        return tutor;
    }


}
