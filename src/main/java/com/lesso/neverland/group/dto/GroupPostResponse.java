package com.lesso.neverland.group.dto;

import com.lesso.neverland.comment.dto.CommentDto;

import java.time.LocalDate;
import java.util.List;

public record GroupPostResponse(String title,
                                String subtitle,
                                String content,
                                LocalDate createdDate,
                                String writer, // 피드 작성자 닉네임
                                String backgroundMusic,
                                String backgroundMusicUrl,
                                String postImage,
                                boolean isLike,
                                List<CommentDto> comments) {}
