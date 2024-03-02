package com.lesso.neverland.comment.dto;

import java.time.LocalDate;

public record CommentDto(Long commentIdx,
                         String writer,
                         String profileImage,
                         LocalDate createdDate,
                         String content) {}
