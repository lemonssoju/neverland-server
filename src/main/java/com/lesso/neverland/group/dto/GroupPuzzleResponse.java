package com.lesso.neverland.group.dto;

import java.time.LocalDate;

public record GroupPuzzleResponse(String title,
                                  String content,
                                  LocalDate createdDate,
                                  String writer, // 피드 작성자 닉네임
                                  String backgroundMusic,
                                  String backgroundMusicUrl,
                                  String puzzleImage) {}
