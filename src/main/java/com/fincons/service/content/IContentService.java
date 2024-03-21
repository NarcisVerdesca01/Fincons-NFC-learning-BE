package com.fincons.service.content;

import com.fincons.dto.ContentDto;
import com.fincons.entity.Content;
import java.util.List;

public interface IContentService {

     Content findById(long id);
     List<Content> findAllContent();

     List<Content> findAllNotAssociatedContent();
     Content createContent(ContentDto contentDto);
     void deleteContent(long id);
     Content updateContent(long id, ContentDto contentDto);
}
