package com.yorath.booksearch.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiResultStatus {

    SUCCESS_REQUEST("S000", "정상적으로 처리되었습니다."),

    MEMBER_ID_DUPLICATED("E101", "회원아이디가 이미 존재합니다."),
    MEMBER_ID_NOT_EXIST("E101", "회원아이디가 존재하지 않습니다."),
    LOGIN_PASSWORD_INVALID("E101", "비밀번호가 틀렸습니다."),

    INTERNAL_SERVER_ERROR("E900", "서비스처리 도중 오류가 발생했습니다.")

    ;

    private String code;
    private String message;

}
