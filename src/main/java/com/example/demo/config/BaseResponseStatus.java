package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),
    EXPIRED_ACCESS_TOKEN(false, 2004, "Access token이 만료되었습니다."),
    INVALID_ACCESS_TOKEN(false, 2005, "유효하지 않은 access token 입니다."),
    INVALID_USER(false, 2006, "존재하지 않는 회원입니다."),

    // User
    USER_EMPTY_NAME(false, 2011, "유저 이름을 확인해주세요."),
    USER_EMPTY_NICKNAME(false, 2012, "유저 아이디 값을 확인해주세요."),
    USER_INVALID_NICKNAME(false, 2012, "사용할 수 없는 아이디입니다."),
    USER_EMPTY_PHONE(false, 2013, "유저 휴대폰 번호를 확인해주세요."),
    USER_INVALID_PHONE(false, 2014, "휴대폰 번호 형식을 확인해주세요."),
    USER_EMPTY_PASSWORD(false, 2015, "비밀번호를 입력해주세요."),
    USER_EMPTY_EMAIL(false, 2016, "이메일을 입력해주세요."),
    USER_INVALID_EMAIL(false, 2017, "이메일 형식을 확인해주세요."),
    USER_EXISTS_EMAIL(false,2018,"중복된 이메일입니다."),
    USER_EXISTS_PHONE(false,2019,"중복된 휴대폰 번호입니다."),
    USER_EXISTS_NICKNAME(false,2020,"중복된 아이디입니다."),
    USER_INVALID_GENDER(false,2021,"성별 정보를 Male/Female 로 입력해 주세요."),
    USER_EMPTY_IMG(false, 2022, "프로필 사진을 업로드 해주세요."),
    USER_NOW_UNAVAILABLE(false, 2023, "이미 비활성화된 회원입니다."),
    USER_NOW_AVAILABLE(false, 2024, "이미 활성화 되어있는 회원입니다."),


    // post
    POST_UNAVAILABLE(false, 2050, "존재하지 않는 게시물입니다."),
    EMPTY_CONTENTS(false, 2051, "내용을 입력해 주세요"),
    COMMENT_LONG_LIMIT(false, 2052, "댓글을 2000자 이하로 입력해 주세요"),

    // store
    STORE_FAILED(false, 2070, "게시물 저장에 실패하였습니다."),
    STORE_CANCEL_FAILED(false, 2070, "게시물 저장 취소에 실패하였습니다."),


    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),
    VALIDATION_ERROR(false, 4002, "값이 유효하지 않습니다."),
    VALIDATION_TYPE_ERROR(false, 4003, "값의 타입이 유효하지 않습니다."),
    VALIDATION_SERVER_ERROR(false, 4004, "서버 문제로 접근에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),
    POST_IMG_EMPTY(false, 5001 ,"사진이나 동영상URL이 비어있습니다." ),
    NONPOSTID_ERROR(false, 5002 ,"조회되는 게시글이 없습니다." ),
    NOTNORMALUSER_ERROR(false, 5003 ,"정상적인 유저의 접근이 아닙니다." );

    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
