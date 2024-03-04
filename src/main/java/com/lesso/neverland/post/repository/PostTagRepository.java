package com.lesso.neverland.post.repository;

import com.lesso.neverland.post.domain.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {
}
