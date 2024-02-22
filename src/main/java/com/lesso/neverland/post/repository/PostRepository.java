package com.lesso.neverland.post.repository;

import com.lesso.neverland.common.enums.Contents;
import com.lesso.neverland.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findTop3ByPostTags_TagNameIn(List<Contents> interests);
}
