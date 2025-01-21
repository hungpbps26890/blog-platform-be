package com.dev.blog.services;

import com.dev.blog.domain.entities.Post;
import com.dev.blog.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {

    List<Post> listPosts(UUID categoryId, UUID tagId);

    List<Post> listDrafts(User user);
}
