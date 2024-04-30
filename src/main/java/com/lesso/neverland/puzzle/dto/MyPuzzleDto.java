package com.lesso.neverland.puzzle.dto;

import java.time.LocalDate;

public record MyPuzzleDto(String puzzleImage,
                          String title,
                          LocalDate createdDate) {}
