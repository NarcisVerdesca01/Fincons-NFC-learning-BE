package com.fincons.service.lessoncontent;

import com.fincons.repository.ContentRepository;
import com.fincons.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LessonContentService implements ILessonContentService{
    
    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ContentRepository contentRepository;



}
