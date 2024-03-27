package com.lesso.neverland.common.enums;

import com.lesso.neverland.common.BaseException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.lesso.neverland.common.BaseResponseStatus.WRONG_CONTENTS_NAME;
import static com.lesso.neverland.common.BaseResponseStatus.WRONG_PREFERENCE_NAME;

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

    private final String contentsName;
    private final Contents parent;
    private final List<Contents> child;

    Contents(String name, Contents parent) {
        this.child = new ArrayList<>();
        this.contentsName = name;
        this.parent = parent;
        if(Objects.nonNull(parent)) {
            parent.child.add(this);
        }
    }

    public static Contents getEnumByName(String name) throws BaseException {
        return Arrays.stream(Contents.values())
                .filter(contents -> contents.getContentsName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new BaseException(WRONG_CONTENTS_NAME));
    }

    public static Contents getPreference(Contents contents, String preference) throws BaseException {
        return Arrays.stream(Contents.values())
                .filter(c -> c.parent == contents && c.getContentsName().equalsIgnoreCase(preference))
                .findFirst()
                .orElseThrow(() -> new BaseException(WRONG_PREFERENCE_NAME));
    }
}
