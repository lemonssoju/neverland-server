package com.lesso.neverland.puzzle.dto;
import java.util.List;

public record PuzzleDetailResponse(String location,
                                   String puzzleImage,
                                   String puzzleDate,
                                   String writer,
                                   String title,
                                   String content,
                                   List<String> memberImageList, // 3명만
                                   Integer memberCount,
                                   List<PuzzlePieceDto> puzzlePieces) {}
