package com.fincons.service.content;

import com.fincons.dto.ContentDto;
import com.fincons.entity.Content;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IContentService {

     Content findById(Long id);
     List<Content> findAllContent();
     Content createContent(ContentDto contentDto);
     void deleteContent(long id);
     Content updateContent(long id, ContentDto contentDto);
}
