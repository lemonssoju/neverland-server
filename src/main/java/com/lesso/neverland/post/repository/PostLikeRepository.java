package com.lesso.neverland.post.repository;

import com.lesso.neverland.post.domain.Post;
import com.lesso.neverland.post.domain.PostLike;
import com.lesso.neverland.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByPostAndUserAndStatusEquals(Post post, User user, String status);
    PostLike findByPostAndUser(Post post, User user);
}
