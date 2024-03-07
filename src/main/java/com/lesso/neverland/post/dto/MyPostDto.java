package com.lesso.neverland.post.dto;

import java.time.LocalDate;
import java.util.List;

public record MyPostDto(String postImage,
                        String title,
                        List<String> tags,
                        LocalDate createdDate) {}
