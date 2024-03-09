package com.fincons.controller.course;


import com.fincons.controller.CourseController;
import com.fincons.mapper.CourseMapper;
import com.fincons.service.course.ICourseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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


    }

}