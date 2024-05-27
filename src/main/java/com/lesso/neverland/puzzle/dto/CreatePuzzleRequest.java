package com.lesso.neverland.puzzle.dto;

import java.util.List;

public record CreatePuzzleRequest(String title,
                                  String puzzleDate,
                                  String location,
                                  String content,
                                  List<Long> puzzlerList) {}
