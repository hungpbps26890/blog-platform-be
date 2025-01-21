package com.dev.blog.controllers;

import com.dev.blog.domain.dtos.*;
import com.dev.blog.domain.entities.Post;
import com.dev.blog.domain.entities.User;
import com.dev.blog.mappers.PostMapper;
import com.dev.blog.services.PostService;
import com.dev.blog.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    private final UserService userService;

    private final PostMapper postMapper;

    @GetMapping
    public ResponseEntity<List<PostDto>> listPosts(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) UUID tagId
    ) {
        List<Post> posts = postService.listPosts(categoryId, tagId);

        List<PostDto> response = posts.stream()
                .map(postMapper::toDto)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/drafts")
    public ResponseEntity<List<PostDto>> listDrafts(@RequestAttribute UUID userId) {
        User loggedInUser = userService.getUserById(userId);

        List<Post> drafts = postService.listDrafts(loggedInUser);

        List<PostDto> response = drafts.stream()
                .map(postMapper::toDto)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @Valid @RequestBody CreatePostRequestDto request,
            @RequestAttribute UUID userId
    ) {
        User loggedInUser = userService.getUserById(userId);

        CreatePostRequest createPostRequest = postMapper.toCreatePostRequest(request);

        Post createdPost = postService.createPost(loggedInUser, createPostRequest);

        PostDto response = postMapper.toDto(createdPost);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePostRequestDto request
    ) {
        UpdatePostRequest updatePostRequest = postMapper.toUpdatePostRequest(request);

        Post updatedPost = postService.updatePost(id, updatePostRequest);

        PostDto response = postMapper.toDto(updatedPost);

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable UUID id) {
        Post post = postService.getPost(id);

        PostDto response = postMapper.toDto(post);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id) {
        postService.deletePost(id);

        return ResponseEntity.noContent().build();
    }
}
