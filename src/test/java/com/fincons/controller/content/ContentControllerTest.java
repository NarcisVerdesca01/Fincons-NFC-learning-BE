package com.fincons.controller.content;


import com.fincons.controller.ContentController;
import com.fincons.dto.AbilityDto;
import com.fincons.dto.ContentDto;
import com.fincons.entity.Ability;
import com.fincons.entity.Content;
import com.fincons.exception.DuplicateException;
import com.fincons.mapper.ContentMapper;
import com.fincons.service.content.IContentService;
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
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class ContentControllerTest {

    @Autowired
    private ContentController contentController;

    @MockBean
    private IContentService iContentService;

    @Autowired
    private ContentMapper contentMapper;


    @Test
    public void testGetAllAbilities_Success() {
        List<Content> contents = Arrays.asList(new Content(1L, "video", "randomContent", null),
                new Content(2L, "video", "randomContent2", null));
        when(iContentService.findAllContent()).thenReturn(contents);
        List<ContentDto> contentDtoList = Arrays.asList(new ContentDto(1L, "video","randomContent",null),
                new ContentDto(2L, "video","randomContent2",null));
        ResponseEntity<ApiResponse<List<ContentDto>>> responseEntity = contentController.getAllContent();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<ContentDto> responseContents = responseEntity.getBody().getData();
        assertNotNull(responseContents);
        assertEquals(2, responseContents.size());
        assertEquals("randomContent", responseContents.get(0).getContent());
        assertEquals("randomContent2", responseContents.get(1).getContent());
    }

    @Test
    public void testGetContentById_Success(){
        Content content = new Content(1L,"video","randomContent",null);
        when(iContentService.findById(1L)).thenReturn(content);
        ResponseEntity<ApiResponse<ContentDto>> responseEntity = contentController.getById(contentMapper.mapContentToContentDto(content).getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ContentDto contentDto = Objects.requireNonNull(responseEntity.getBody()).getData();
        assertNotNull(contentDto);
        assertEquals(1, contentDto.getId());
        assertEquals(content.getId(), contentDto.getId());
        assertEquals(content.getContent(), contentDto.getContent());
    }

    @Test
    public void testCreateContent_Success() throws DuplicateException {
        ContentDto inputContentDto = new ContentDto(1L,"video","randomContent",null);
        when(iContentService.createContent(inputContentDto)).thenReturn(contentMapper.mapContentDtoToContentEntity(inputContentDto));
        ResponseEntity<ApiResponse<ContentDto>> responseEntity = contentController.createContent(inputContentDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(inputContentDto.getContent(),responseEntity.getBody().getData().getContent());
    }

    @Test
    public void testUpdateContent_Success() throws DuplicateException {
        long contentId = 1L;
        ContentDto inputContentDto = new ContentDto(1L,"video","randomContent",null);
        Content updatedContent = new Content(1L,"video","randomContent",null);
        when(iContentService.updateContent(contentId, inputContentDto)).thenReturn(updatedContent);
        ResponseEntity<ApiResponse<String>> responseEntity = contentController.updateContent(contentId, inputContentDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("The content has been successfully updated!", responseEntity.getBody().getData());
    }


    //TODO UPDATECONTENT EXCEPTION
}
