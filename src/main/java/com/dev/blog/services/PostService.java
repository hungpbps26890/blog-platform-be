package com.dev.blog.services;

import com.dev.blog.domain.dtos.CreatePostRequest;
import com.dev.blog.domain.dtos.UpdatePostRequest;
import com.dev.blog.domain.entities.Post;
import com.dev.blog.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {

    List<Post> listPosts(UUID categoryId, UUID tagId);

    List<Post> listDrafts(User user);

    Post createPost(User user, CreatePostRequest request);

    Post updatePost(UUID id, UpdatePostRequest request);

    Post getPost(UUID id);
}
