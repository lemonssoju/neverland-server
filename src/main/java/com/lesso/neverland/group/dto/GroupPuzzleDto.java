package com.lesso.neverland.group.dto;

public record GroupPuzzleDto(Long puzzleIdx,
                             String title,
                             String puzzleImage,
                             String writer,
                             String createdDate,
                             String location) {}
