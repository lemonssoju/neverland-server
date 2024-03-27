package com.lesso.neverland.post.dto;

public record EditPostRequest(String title,
                              String subtitle,
                              String contentsType,
                              String backgroundMusic,
                              String backgroundMusicUrl,
                              String postImage,
                              String content) {
}
