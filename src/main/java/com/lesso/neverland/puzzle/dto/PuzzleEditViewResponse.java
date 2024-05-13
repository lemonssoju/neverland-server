package com.lesso.neverland.puzzle.dto;

public record PuzzleEditViewResponse(String title,
                                     String backgroundMusic,
                                     String backgroundMusicUrl,
                                     String puzzleImage,
                                     String content) {
}