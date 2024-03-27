package com.lesso.neverland.group.dto;

public record GroupPostRequest(String title,
                               String subtitle,
                               String contentsType,
                               String backgroundMusic,
                               String backgroundMusicUrl,
                               String postImage,
                               String content) {}
