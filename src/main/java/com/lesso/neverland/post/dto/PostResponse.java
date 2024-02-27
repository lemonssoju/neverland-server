package com.lesso.neverland.post.dto;

import java.time.LocalDate;
import java.util.List;

public record PostResponse(String title,
                           String subtitle,
                           String content,
                           LocalDate createdDate,
                           String writer, // 피드 작성자 닉네임
                           String backgroundMusic,
                           String backgroundMusicUrl,
                           String postImage,
                           boolean isLike,
                           List<RecommendedPostDto> posts,
                           List<CommentDto> comments) {
    public record RecommendedPostDto(String postImage, String title, List<String> tags) {}
    public record CommentDto(Long commentIdx,
                             String writer,
                             String profileImage,
                             LocalDate createdDate,
                             String content) {}
}
