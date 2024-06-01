package com.lesso.neverland.puzzle.dto;

public record KakaoApiResponse(Documents[] documents) {

    public record Documents(String address, String x, String y) {
    }
}

