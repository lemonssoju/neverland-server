package com.lesso.neverland.comment.repository;

import com.lesso.neverland.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
