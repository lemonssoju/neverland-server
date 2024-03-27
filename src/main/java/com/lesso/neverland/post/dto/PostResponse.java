package com.lesso.neverland.post.dto;

import com.lesso.neverland.comment.dto.CommentDto;

import java.time.LocalDate;
import java.util.List;

public record PostResponse(String title,
                           String subtitle,
                           String content,
                           LocalDate createdDate,
                           Long writerIdx,
                           String writer, // 피드 작성자 닉네임
                           String backgroundMusic,
                           String backgroundMusicUrl,
                           String postImage,
                           boolean isLike,
                           List<RecommendedPostDto> posts,
                           List<CommentDto> comments) {}
