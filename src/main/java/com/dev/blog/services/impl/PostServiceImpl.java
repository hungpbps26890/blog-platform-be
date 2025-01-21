package com.dev.blog.services.impl;

import com.dev.blog.domain.dtos.CreatePostRequest;
import com.dev.blog.domain.dtos.UpdatePostRequest;
import com.dev.blog.domain.entities.Category;
import com.dev.blog.domain.entities.Post;
import com.dev.blog.domain.entities.Tag;
import com.dev.blog.domain.entities.User;
import com.dev.blog.domain.enums.PostStatus;
import com.dev.blog.repositories.PostRepository;
import com.dev.blog.services.CategoryService;
import com.dev.blog.services.PostService;
import com.dev.blog.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final CategoryService categoryService;

    private final TagService tagService;

    private static final int WORDS_PER_MINUTE = 200;

    @Transactional(readOnly = true)
    @Override
    public List<Post> listPosts(UUID categoryId, UUID tagId) {
        if (categoryId != null && tagId != null) {
            Category category = categoryService.getCategoryById(categoryId);
            Tag tag = tagService.getTagById(tagId);

            return postRepository.findAllByStatusAndCategoryAndTagsContaining(
                    PostStatus.PUBLISHED,
                    category,
                    tag
            );
        }

        if (categoryId != null) {
            Category category = categoryService.getCategoryById(categoryId);

            return postRepository.findAllByStatusAndCategory(
                    PostStatus.PUBLISHED,
                    category
            );
        }

        if (tagId != null) {
            Tag tag = tagService.getTagById(tagId);

            return postRepository.findAllByStatusAndTagsContaining(
                    PostStatus.PUBLISHED,
                    tag
            );
        }

        return postRepository.findAllByStatus(PostStatus.PUBLISHED);
    }

    @Override
    public List<Post> listDrafts(User user) {
        return postRepository.findAllByStatusAndAuthor(PostStatus.DRAFT, user);
    }

    @Transactional
    @Override
    public Post createPost(User user, CreatePostRequest request) {
        Post newPost = new Post();

        newPost.setTitle(request.getTitle());
        newPost.setContent(request.getContent());
        newPost.setStatus(request.getStatus());
        newPost.setAuthor(user);
        newPost.setReadingTime(calculateReadingTime(request.getContent()));

        Category category = categoryService.getCategoryById(request.getCategoryId());
        newPost.setCategory(category);

        Set<UUID> tagIds = request.getTagIds();
        List<Tag> tags = tagService.listTagsByIds(tagIds);
        newPost.setTags(new HashSet<>(tags));

        return postRepository.save(newPost);
    }

    @Transactional
    @Override
    public Post updatePost(UUID id, UpdatePostRequest request) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id " + id));

        existingPost.setTitle(request.getTitle());
        existingPost.setContent(request.getContent());
        existingPost.setStatus(request.getStatus());
        existingPost.setReadingTime(calculateReadingTime(request.getContent()));

        UUID updateCategoryId = request.getCategoryId();

        if (!existingPost.getCategory().getId().equals(updateCategoryId)) {
            Category newCategory = categoryService.getCategoryById(updateCategoryId);

            existingPost.setCategory(newCategory);
        }

        Set<UUID> existingTagIds = existingPost.getTags()
                .stream()
                .map(Tag::getId)
                .collect(Collectors.toSet());

        Set<UUID> updateTagIds = request.getTagIds();

        if (!existingTagIds.equals(updateTagIds)) {
            List<Tag> newTags = tagService.listTagsByIds(updateTagIds);

            existingPost.setTags(new HashSet<>(newTags));
        }

        return postRepository.save(existingPost);
    }

    @Override
    public Post getPost(UUID id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id " + id));
    }

    private Integer calculateReadingTime(String content) {
        if (content == null || content.isEmpty()) {
            return 0;
        }

        int wordCount = content.trim().split("\\s+").length;

        return (int) Math.ceil((double) wordCount / WORDS_PER_MINUTE);
    }
}
