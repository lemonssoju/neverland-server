package com.lesso.neverland.common.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public enum Contents {

    MOVIE("영화", null),
        HORROR("호러", MOVIE),
        ROMANCE("로맨스", MOVIE),
    DRAMA("드라마", null),
    ANIMATION("애니메이션", null),
    FASHION("패션", null),
    MUSIC("음악", null),
    ENTERTAINMENT("예능", null);

    private final String name;
    private final Contents parent;
    private final List<Contents> child;

    Contents(String name, Contents parent) {
        this.child = new ArrayList<>();
        this.name = name;
        this.parent = parent;
        if(Objects.nonNull(parent)) {
            parent.child.add(this);
        }
    }
}
