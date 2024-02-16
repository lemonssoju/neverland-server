package com.lesso.neverland.common;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    /**
     * 1000: 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000: Request 오류
     */
    // user(2000-2099)
    UNMATCHED_PASSWORD(false, 2001, "비밀번호가 일치하지 않습니다."),

    // interest(2100-2199)
    WRONG_CONTENTS_NAME(false, 2100, "콘텐츠 이름이 잘못되었습니다."),
    WRONG_PREFERENCE_NAME(false, 2101, "취향 이름이 잘못되었습니다."),


    /**
     * 3000: Response 오류
     */


    /**
     * 4000: DB, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패했습니다.");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
