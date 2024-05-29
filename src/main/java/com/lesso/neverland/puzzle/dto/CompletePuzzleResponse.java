package com.lesso.neverland.puzzle.dto;

public record CompletePuzzleResponse(String prompt,
                                     String description,
                                     Long albumIdx) {}
