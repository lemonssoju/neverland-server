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
    INVALID_USER_IDX(false, 2000, "잘못된 user idx 입니다."),
    UNMATCHED_PASSWORD(false, 2001, "비밀번호가 일치하지 않습니다."),
    INVALID_PASSWORD(false, 2002, "형식이 잘못된 비밀번호입니다."),
    INVALID_REFRESH_TOKEN(false, 2003, "잘못된 refresh token입니다."),
    NO_MATCH_USER(false, 2004, "일치하는 user가 없습니다."),
    EXPIRED_REFRESH_TOKEN(false, 2005, "만료된 refresh token입니다."),
    DUPLICATED_NICKNAME(false, 2006, "중복된 닉네임입니다."),
    DUPLICATED_LOGIN_ID(false, 2007, "중복된 아이디입니다."),
    NULL_ACCESS_TOKEN(false, 2008, "access token이 비었습니다."),
    WRONG_PASSWORD(false, 2009, "잘못된 password 입니다."),

    // interest(2100-2199)
    WRONG_CONTENTS_NAME(false, 2100, "콘텐츠 이름이 잘못되었습니다."),
    WRONG_PREFERENCE_NAME(false, 2101, "취향 이름이 잘못되었습니다."),

    // post(2200-2299)
    INVALID_POST_IDX(false, 2200, "잘못된 post idx 입니다."),
    ALREADY_DELETED_POST(false, 2201, "이미 삭제된 post 입니다."),
    NO_POST_WRITER(false, 2202, "해당 post의 작성자가 아닙니다."),

    // group(2300-2399)
    INVALID_GROUP_IDX(false, 2300, "잘못된 group idx 입니다."),

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
