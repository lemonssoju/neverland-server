package com.lesso.neverland.puzzle.dto;
import java.util.List;

public record PuzzleDetailResponse(String location,
                                   String puzzleImage,
                                   String puzzleDate,
                                   String createdDate,
                                   String writer,
                                   String title,
                                   String content,
                                   List<String> memberImageList, // 3명만
                                   List<String> memberNicknameList,
                                   Integer memberCount,
                                   Integer writeCount, // PuzzlePieceCount+1
                                   boolean isWriter,
                                   boolean hasWrite, // 해당 user가 퍼즐피스 작성했는지 여부
                                   boolean hasAlbum,
                                   List<PuzzlePieceDto> puzzlePieces) {}
