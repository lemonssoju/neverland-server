package com.lesso.neverland.album.dto;

import com.lesso.neverland.comment.dto.CommentDto;

import java.util.List;

public record AlbumDetailResponse(String title,
                                  String puzzleDate,
                                  String location,
                                  List<String> memberList,
                                  String albumImage,
                                  String description,
                                  Long puzzleIdx,
                                  List<CommentDto> commentList) {
}
