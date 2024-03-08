package com.lesso.neverland.common.enums;

import lombok.Getter;

import java.util.Arrays;

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

    // order 값으로 label 찾기
    public static ThumbnailOrder valueOfOrder(Integer order) {
        return Arrays.stream(values())
                .filter(thumbnailOrder -> thumbnailOrder.order.equals(order))
                .findAny()
                .orElse(null);
    }
}
