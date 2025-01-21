package com.dev.blog.controllers;

import com.dev.blog.domain.dtos.CreateTagRequest;
import com.dev.blog.domain.dtos.TagResponse;
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
    public ResponseEntity<List<TagResponse>> listTags() {
        List<Tag> tags = tagService.listTags();

        List<TagResponse> response = tags.stream()
                .map(tagMapper::toTagResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<List<TagResponse>> createTags(@RequestBody CreateTagRequest request) {
        List<Tag> savedTags = tagService.createTags(request.getNames());

        List<TagResponse> response = savedTags.stream()
                .map(tagMapper::toTagResponse)
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
