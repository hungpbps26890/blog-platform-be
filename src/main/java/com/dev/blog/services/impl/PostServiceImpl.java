package com.dev.blog.services.impl;

import com.dev.blog.domain.entities.Category;
import com.dev.blog.domain.entities.Post;
import com.dev.blog.domain.entities.Tag;
import com.dev.blog.domain.enums.PostStatus;
import com.dev.blog.repositories.PostRepository;
import com.dev.blog.services.CategoryService;
import com.dev.blog.services.PostService;
import com.dev.blog.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final CategoryService categoryService;

    private final TagService tagService;

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
}
