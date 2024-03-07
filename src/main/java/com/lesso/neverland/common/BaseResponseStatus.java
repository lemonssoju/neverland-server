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
    // user(2000-2049)
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

    // userprofile(2050-2099)
    BLANK_NICKNAME(false, 2050, "nickname이 비었습니다."),


    // interest(2100-2199)
    WRONG_CONTENTS_NAME(false, 2100, "콘텐츠 이름이 잘못되었습니다."),
    WRONG_PREFERENCE_NAME(false, 2101, "취향 이름이 잘못되었습니다."),

    // post(2200-2299)
    INVALID_POST_IDX(false, 2200, "잘못된 post idx 입니다."),
    ALREADY_DELETED_POST(false, 2201, "이미 삭제된 post 입니다."),
    NO_POST_WRITER(false, 2202, "해당 post의 작성자가 아닙니다."),

    // group(2300-2399)
    INVALID_GROUP_IDX(false, 2300, "잘못된 group idx 입니다."),
    NO_GROUP_POST(false, 2301, "이 글은 해당 그룹의 글이 아닙니다."),
    NO_GROUP_ADMIN(false, 2302, "해당 그룹의 관리자가 아닙니다."),
    BLANK_GROUP_NAME(false, 2303, "그룹명이 비었습니다."),
    BLANK_GROUP_SUB_NAME(false, 2304, "그룹 subname이 비었습니다."),
    NULL_GROUP_IMAGE(false, 2305, "그룹 이미지가 비었습니다."),
    NO_GROUP_MEMBER(false, 2306, "그룹 멤버가 아닙니다."),
    GROUP_ADMIN(false, 2307, "그룹 관리자는 그룹을 나갈 수 없습니다."),

    // image(2400-2499)
    IMAGE_DELETE_FAIL(false, 2400, "이미지 삭제에 실패했습니다."),

    // comment(2500-2599)
    INVALID_COMMENT_IDX(false, 2500, "잘못된 comment idx 입니다."),
    NO_COMMENT_WRITER(false, 2501, "댓글 작성자가 아닙니다."),
    ALREADY_DELETED_COMMENT(false, 2502, "이미 삭제된 댓글입니다."),
    BLANK_COMMENT_CONTENT(false, 2503, "댓글 내용이 비었습니다."),

    // guest memo(2600-2699)
    TOO_LONG_CONTENT(false, 2600, "내용은 50자 이하여야 합니다."),


    /**
     * 3000: Response 오류
     */
    // user(3000-3099)
    INVALID_NICKNAME(false, 3000, "해당 닉네임으로 유저를 찾을 수 없습니다."),


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
