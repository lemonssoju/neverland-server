package com.lesso.neverland.comment.dto;

public record CommentDto(Long commentIdx,
                         String writer,
                         String profileImage,
                         String createdDate,
                         String content) {}
