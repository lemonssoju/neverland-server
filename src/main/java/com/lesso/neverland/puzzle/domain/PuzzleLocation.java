package com.lesso.neverland.puzzle.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PuzzleLocation {

    @Column(nullable = false)
    private String location;

    private String x;
    private String y;

    @Builder
    public PuzzleLocation(String location) {this.location = location;}
}
