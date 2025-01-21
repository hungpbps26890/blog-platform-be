package com.dev.blog.controllers;

import com.dev.blog.domain.dtos.CreateTagRequest;
import com.dev.blog.domain.dtos.TagDto;
import com.dev.blog.domain.entities.Tag;
import com.dev.blog.mappers.TagMapper;
import com.dev.blog.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    private final TagMapper tagMapper;

    @GetMapping
    public ResponseEntity<List<TagDto>> listTags() {
        List<Tag> tags = tagService.listTags();

        List<TagDto> response = tags.stream()
                .map(tagMapper::toDto)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<List<TagDto>> createTags(@RequestBody CreateTagRequest request) {
        List<Tag> savedTags = tagService.createTags(request.getNames());

        List<TagDto> response = savedTags.stream()
                .map(tagMapper::toDto)
                .toList();

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id) {
        tagService.deleteTag(id);

        return ResponseEntity.noContent().build();
    }

}
