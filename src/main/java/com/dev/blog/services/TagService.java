package com.dev.blog.services;

import com.dev.blog.domain.entities.Tag;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface TagService {

    List<Tag> listTags();

    List<Tag> createTags(Set<String> names);

    void deleteTag(UUID id);

    Tag getTagById(UUID id);

    List<Tag> listTagsByIds(Set<UUID> ids);
}
