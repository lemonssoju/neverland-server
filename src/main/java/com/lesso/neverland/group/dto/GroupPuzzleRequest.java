package com.lesso.neverland.group.dto;

public record GroupPuzzleRequest(String title,
                                 String content,
                                 String puzzleImage,
                                 String puzzleDate,
                                 String location,
                                 String backgroundMusic,
                                 String backgroundMusicUrl) {}
