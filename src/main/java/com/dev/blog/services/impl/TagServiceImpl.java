package com.dev.blog.services.impl;

import com.dev.blog.domain.entities.Tag;
import com.dev.blog.repositories.TagRepository;
import com.dev.blog.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<Tag> listTags() {
        return tagRepository.findAllWithPostCount();
    }

    @Transactional
    @Override
    public List<Tag> createTags(Set<String> names) {
        List<Tag> existingTags = tagRepository.findByNameInIgnoreCase(names);

        Set<String> existingNames = existingTags
                .stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());

        List<Tag> newTags = names.stream()
                .filter(name -> !existingNames.contains(name))
                .map(name -> Tag.builder()
                        .name(name)
                        .posts(new HashSet<>())
                        .build()
                )
                .toList();

        List<Tag> savedTags = new ArrayList<>();

        if (!newTags.isEmpty()) {
            savedTags = tagRepository.saveAll(newTags);
        }

        savedTags.addAll(existingTags);

        return savedTags;
    }

    @Override
    public void deleteTag(UUID id) {
        tagRepository.findById(id)
                .ifPresent(tag -> {
                    if (!tag.getPosts().isEmpty()) {
                        throw new IllegalStateException("Cannot delete tag with posts");
                    }

                    tagRepository.deleteById(id);
                });
    }

    @Override
    public Tag getTagById(UUID id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with id " + id));
    }
}
