package com.lesso.neverland.puzzle.dto;
import java.time.LocalDate;

public record PuzzleResponse(String title,
                             String content,
                             LocalDate createdDate,
                             Long writerIdx,
                             String writer, // 피드 작성자 닉네임
                             String backgroundMusic,
                             String backgroundMusicUrl,
                             String puzzleImage) {}
