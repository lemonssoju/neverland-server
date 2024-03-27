package com.lesso.neverland.post.repository;

import com.lesso.neverland.common.enums.Contents;
import com.lesso.neverland.post.domain.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {
    List<PostTag> findByTagName(Contents tagName);
}
