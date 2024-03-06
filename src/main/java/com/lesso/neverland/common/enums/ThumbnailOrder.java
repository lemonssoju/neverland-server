package com.lesso.neverland.common.enums;

import lombok.Getter;

@Getter
public enum ThumbnailOrder {
    LEFT_TOP(1),
    RIGHT_TOP(2),
    LEFT_BOTTOM(3),
    RIGHT_BOTTOM(4);

    private final Integer order;

    ThumbnailOrder(Integer order) {
        this.order = order;
    }
}
