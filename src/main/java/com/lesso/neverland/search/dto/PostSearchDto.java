package com.lesso.neverland.search.dto;

import java.util.List;

public record PostSearchDto(Long postIdx,
                            String contentsType,
                            String title,
                            String postImage,
                            List<String> tags) {}
