package com.lesso.neverland.common.base;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BaseResponseStatus {

    /**
     * 1000: 요청 성공
     */
    SUCCESS(true, HttpStatus.OK, "요청에 성공하였습니다."),


    /**
     * 2000: Request 오류
     */
    // user(2000-2049)
    INVALID_USER_IDX(false, HttpStatus.BAD_REQUEST, "잘못된 user idx 입니다."),
    INVALID_PASSWORD(false, HttpStatus.BAD_REQUEST, "형식이 잘못된 비밀번호입니다."),
    NO_MATCH_USER(false, HttpStatus.BAD_REQUEST, "일치하는 user가 없습니다."),
    WRONG_PASSWORD(false, HttpStatus.BAD_REQUEST, "잘못된 password 입니다."),
    INVALID_ACCESS_TOKEN(false, HttpStatus.BAD_REQUEST, "잘못된 access token 입니다."),
    INVALID_REFRESH_TOKEN(false, HttpStatus.BAD_REQUEST, "잘못된 refresh token 입니다."),
    NULL_ACCESS_TOKEN(false, HttpStatus.BAD_REQUEST, "access token을 입력해주세요."),
    INVALID_JWT_SIGNATURE(false, HttpStatus.BAD_REQUEST, "유효하지 않은 JWT 시그니처입니다."),
    EXPIRED_ACCESS_TOKEN(false, HttpStatus.BAD_REQUEST, "만료된 토큰입니다."),
    UNSUPPORTED_JWT_TOKEN(false, HttpStatus.BAD_REQUEST, "지원하지 않는 JWT 토큰 형식입니다."),
    EMPTY_JWT_CLAIM(false, HttpStatus.BAD_REQUEST, "JWT claims string이 비었습니다."),
    EXPIRED_REFRESH_TOKEN(false, HttpStatus.BAD_REQUEST, "만료된 refresh token 입니다."),
    ACCESS_DENIED(false, HttpStatus.BAD_REQUEST, "접근 권한이 없습니다."),
    UNMATCHED_PASSWORD(false, HttpStatus.CONFLICT, "비밀번호가 일치하지 않습니다."),
    DUPLICATED_NICKNAME(false, HttpStatus.CONFLICT, "중복된 닉네임입니다."),
    DUPLICATED_LOGIN_ID(false, HttpStatus.CONFLICT, "중복된 아이디입니다."),


    // userprofile(2050-2099)
    BLANK_NICKNAME(false, HttpStatus.BAD_REQUEST, "nickname이 비었습니다."),


    // interest(2100-2199)
    WRONG_CONTENTS_NAME(false, HttpStatus.BAD_REQUEST, "콘텐츠 이름이 잘못되었습니다."),
    WRONG_PREFERENCE_NAME(false, HttpStatus.BAD_REQUEST, "취향 이름이 잘못되었습니다."),

    // puzzle(2200-2299)
    INVALID_PUZZLE_IDX(false, HttpStatus.BAD_REQUEST, "잘못된 puzzle idx 입니다."),
    NO_PUZZLE_WRITER(false, HttpStatus.BAD_REQUEST, "해당 post의 작성자가 아닙니다."),
    ALREADY_DELETED_PUZZLE(false, HttpStatus.CONFLICT, "이미 삭제된 puzzle 입니다."),
    NO_PUZZLER(false, HttpStatus.FORBIDDEN, "해당 퍼즐의 퍼즐러가 아닙니다."),
    TOO_LONG_CONTENT(false, HttpStatus.BAD_REQUEST, "퍼즐피스의 길이는 100자 이하여야 합니다."),


    // group(2300-2399)
    INVALID_GROUP_IDX(false, HttpStatus.BAD_REQUEST, "잘못된 group idx 입니다."),
    NO_GROUP_PUZZLE(false, HttpStatus.BAD_REQUEST, "이 글은 해당 그룹의 글이 아닙니다."),
    BLANK_GROUP_NAME(false, HttpStatus.BAD_REQUEST, "그룹명이 비었습니다."),
    BLANK_GROUP_SUB_NAME(false, HttpStatus.BAD_REQUEST, "그룹 subname이 비었습니다."),
    NULL_GROUP_IMAGE(false, HttpStatus.BAD_REQUEST, "그룹 이미지가 비었습니다."),
    GROUP_ADMIN(false, HttpStatus.BAD_REQUEST, "그룹 관리자는 그룹을 나갈 수 없습니다."),

    NO_GROUP_MEMBER(false, HttpStatus.FORBIDDEN, "그룹 멤버가 아닙니다."),
    NO_GROUP_ADMIN(false, HttpStatus.FORBIDDEN, "해당 그룹의 관리자가 아닙니다."),
    BLANK_GROUP_START_DATE(false, HttpStatus.BAD_REQUEST, "시작 일자가 비었습니다."),
    NO_MATCH_GROUP(false, HttpStatus.BAD_REQUEST, "참여 코드와 일치하는 그룹이 없습니다."),

    // image(2400-2499)
    IMAGE_DELETE_FAIL(false, HttpStatus.CONFLICT, "이미지 삭제에 실패했습니다."),
    IMAGE_UPLOAD_FAIL(false, HttpStatus.CONFLICT, "이미지 업로드에 실패했습니다."),

    // comment(2500-2599)
    INVALID_COMMENT_IDX(false, HttpStatus.BAD_REQUEST, "잘못된 comment idx 입니다."),
    BLANK_COMMENT_CONTENT(false, HttpStatus.BAD_REQUEST, "댓글 내용이 비었습니다."),
    NO_COMMENT_WRITER(false, HttpStatus.FORBIDDEN, "댓글 작성자가 아닙니다."),
    ALREADY_DELETED_COMMENT(false, HttpStatus.CONFLICT, "이미 삭제된 댓글입니다."),

    // album(2500-2599)
    INVALID_ALBUM_IDX(false, HttpStatus.BAD_REQUEST, "잘못된 album idx 입니다."),


    /**
     * 3000: Response 오류
     */
    // user(3000-3099)
    INVALID_NICKNAME(false, HttpStatus.BAD_REQUEST, "해당 닉네임으로 유저를 찾을 수 없습니다."),


    /**
     * 4000: DB, Server 오류
     */
    DATABASE_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 연결에 실패했습니다."),
    DUPLICATE_RESOURCE(false, HttpStatus.CONFLICT, "데이터가 이미 존재합니다");

    private final boolean isSuccess;
    private final HttpStatus httpStatus;
    private final String message;

    BaseResponseStatus(boolean isSuccess, HttpStatus status, String message) {
        this.isSuccess = isSuccess;
        this.httpStatus = status;
        this.message = message;
    }
}
