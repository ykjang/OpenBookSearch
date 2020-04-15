package com.yorath.booksearch.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonResultStatus {

    SUCCESS_REQUEST("S000", "정상적으로 처리되었습니다."),

    ERROR_MEMBER_ID_DUPLICATED("E101", "회원아이디가 이미 존재합니다."),
    ERROR_INTERNAL_SERVER("E900", "서비스처리 도중 오류가 발생했습니다.")

    ;

    private String code;
    private String message;

}
