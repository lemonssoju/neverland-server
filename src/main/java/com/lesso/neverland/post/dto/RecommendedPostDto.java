package com.lesso.neverland.post.dto;

import java.util.List;

public record RecommendedPostDto(String postImage,
                                 String title,
                                 List<String> tags) {}
