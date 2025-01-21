package com.dev.blog.services.impl;

import com.dev.blog.domain.entities.Tag;
import com.dev.blog.repositories.TagRepository;
import com.dev.blog.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<Tag> listTags() {
        return tagRepository.findAllWithPostCount();
    }
}
