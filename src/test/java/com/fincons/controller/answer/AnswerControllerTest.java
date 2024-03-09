package com.fincons.controller.answer;

import com.fincons.controller.AnswerController;
import com.fincons.dto.AnswerDto;
import com.fincons.entity.Answer;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.AnswerMapper;
import com.fincons.service.answer.IAnswerService;
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
public class AnswerControllerTest {


    @Autowired
    private AnswerController answerController;
    @MockBean
    private IAnswerService iAnswerService;
    @Autowired
    private AnswerMapper answerMapper;


    @Test
    public void testGetAllAnswers_Success(){
        List<Answer> answersList = Arrays.asList(new Answer(1L,"JEE: Java Enterprise Edition",null,true),
                new Answer(2L,"JEE: Java Enterprise Enter",null,false));
        when(iAnswerService.findAllAnswer()).thenReturn(answersList);
        List<AnswerDto> answerDtoList = answersList.stream().map(a -> answerMapper.mapAnswerToAnswerDto(a)).toList();
        ResponseEntity<ApiResponse<List<AnswerDto>>> responseEntity = answerController.getAllAnswer();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<AnswerDto> responseAbilities = Objects.requireNonNull(responseEntity.getBody()).getData();
        assertNotNull(responseAbilities);
        assertEquals(2, responseAbilities.size());
        assertEquals("JEE: Java Enterprise Edition", responseAbilities.get(0).getText());
        assertEquals("JEE: Java Enterprise Enter", responseAbilities.get(1).getText());
    }

    @Test
    public void testGetAnswerById_Success() throws DuplicateException {
        Answer answer = new Answer(1L, "JEE: Java Enterprise Edition",null,true);
        when(iAnswerService.findById(1L)).thenReturn(answer);
        ResponseEntity<ApiResponse<AnswerDto>> responseEntity = answerController.getById(answerMapper.mapAnswerToAnswerDto(answer).getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(Objects.requireNonNull(responseEntity.getBody()).getData());
        assertEquals(answer.getId(), responseEntity.getBody().getData().getId());
        assertEquals(answer.getText(), responseEntity.getBody().getData().getText());
    }

    @Test
    public void testCreateAnswer_Success() throws DuplicateException {
        AnswerDto inputAnswerDto = new AnswerDto(1L,"JEE: Java Enterprise Edition",null,true);
        when(iAnswerService.createAnswer(inputAnswerDto)).thenReturn(answerMapper.mapAnswerDtoToAnswerEntity(inputAnswerDto));
        ResponseEntity<ApiResponse<AnswerDto>> responseEntity = answerController.createAnswer(inputAnswerDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(inputAnswerDto.getText(), Objects.requireNonNull(responseEntity.getBody()).getData().getText());
    }

    @Test
    public void testCreateAnswer_BadRequest() throws DuplicateException {
        AnswerDto inputAnswerDto = new AnswerDto(1L,null,null,true);
        when(iAnswerService.createAnswer(inputAnswerDto)).thenThrow(new IllegalArgumentException("User must enter the text of answer!"));
        ResponseEntity<ApiResponse<AnswerDto>> responseEntity = answerController.createAnswer(inputAnswerDto);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("User must enter the text of answer!", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    public void testUpdateAnswer_Success() throws DuplicateException {
        long answerId = 1L;
        AnswerDto inputAnswerDto = new AnswerDto(1L,"JEE: Java Enterprise Edition",null,true);
        Answer updatedAnswer = new Answer(1L,"JEE: Java Enterprise Edition",null,true);
        when(iAnswerService.updateAnswer(answerId, inputAnswerDto)).thenReturn(updatedAnswer);
        ResponseEntity<ApiResponse<String>> responseEntity = answerController.updateAnswer(answerId, inputAnswerDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("The answer has been successfully updated!", Objects.requireNonNull(responseEntity.getBody()).getData());
    }

    @Test
    public void testUpdateAnswer_ResourceNotFound() throws DuplicateException {
        long nonExistingAnswerId = 999L;
        AnswerDto inputAnswerDto = new AnswerDto();
        when(iAnswerService.updateAnswer(nonExistingAnswerId, inputAnswerDto))
                .thenThrow(new ResourceNotFoundException("Ability does not exist."));
        ResponseEntity<ApiResponse<String>> responseEntity = answerController.updateAnswer(nonExistingAnswerId, inputAnswerDto);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Ability does not exist.", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    public void testUpdateAnswer_IllegalArgumentException() throws DuplicateException {
        long nonExistingAnswerId = 999L;
        AnswerDto inputAnswerDto = new AnswerDto();
        when(iAnswerService.updateAnswer(nonExistingAnswerId, inputAnswerDto))
                .thenThrow(new IllegalArgumentException("Text of answer is null"));
        ResponseEntity<ApiResponse<String>> responseEntity = answerController.updateAnswer(nonExistingAnswerId, inputAnswerDto);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Text of answer is null", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    public void testDeleteAnswer_Success() {
        long answerId = 1L;
        ResponseEntity<ApiResponse<String>> responseEntity = answerController.deleteAnswer(answerId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("The answer has been successfully deleted!", Objects.requireNonNull(responseEntity.getBody()).getData());
        verify(iAnswerService).deleteAnswer(answerId);
    }

    @Test
    public void testDeleteAnswer_ResourceNotFound() {
        long nonExistingAnswerId = 999L;
        doThrow(new ResourceNotFoundException("The answer does not exist")).when(iAnswerService).deleteAnswer(nonExistingAnswerId);
        ResponseEntity<ApiResponse<String>> responseEntity = answerController.deleteAnswer(nonExistingAnswerId);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("The answer does not exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }





}
