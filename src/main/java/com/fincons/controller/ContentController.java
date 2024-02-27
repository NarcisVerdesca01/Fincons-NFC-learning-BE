package com.fincons.controller;

import com.fincons.dto.ContentDto;
import com.fincons.dto.CourseDto;
import com.fincons.dto.LessonDto;
import com.fincons.exception.CourseException;
import com.fincons.mapper.ContentMapper;
import com.fincons.mapper.CourseMapper;
import com.fincons.service.content.ContentService;
import com.fincons.service.content.IContentService;
import com.fincons.utility.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("${application.context}")
public class ContentController {

    @Autowired
    private IContentService iContentService;
    @Autowired
    private ContentMapper contentMapper;

    @GetMapping("${content.get-all-content}")
    public ResponseEntity<ApiResponse<List<ContentDto>>> getAllLessons(){
        List<ContentDto> contentDtoList= iContentService.findAllContent()
                .stream()
                .map(s->contentMapper.mapContentToContentDto(s))
                .toList();
        return ResponseEntity.ok().body(ApiResponse.<List<ContentDto>>builder()
                .data(contentDtoList)
                .build());
    }

    @PostMapping("${content.create}")
    public ResponseEntity<ApiResponse<ContentDto>> createContent(@RequestBody MultipartFile importedFile) {
        try {
            ContentDto contentDtoToShow = contentMapper.mapContentToContentDto(iContentService.createContent(importedFile));
            return ResponseEntity.ok().body(ApiResponse.<ContentDto>builder()
                    .data(contentDtoToShow)
                    .build());
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(ApiResponse.<ContentDto>builder()
                    .message(exception.getMessage())
                    .build());
        }
    }


}
