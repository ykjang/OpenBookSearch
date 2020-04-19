package com.yorath.booksearch.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiResultStatus {

    // SXXX - 공통 성공메세지 코드
    REQUEST_SUCCESS("S000", "정상적으로 처리되었습니다."),

    // E0xx - 공통예러
    REQUEST_SESSION_EXPIRED("E000", "세션이 만료되었습다. 다시 로그인 하세요."),
    REQUEST_PARAM_INVALID("E001", "요청항목이 유효하지 않습니다."),
    REQUEST_METHOD_NOT_ALLOWED("E002", "지원하지 않는 메서드입니다."),

    // E1XX - 사용자관련 에러
    MEMBER_ID_DUPLICATED("E101", "회원아이디가 이미 존재합니다."),
    MEMBER_ID_NOT_EXIST("E102", "회원아이디가 존재하지 않습니다."),
    LOGIN_PASSWORD_INVALID("E102", "비밀번호가 틀렸습니다."),

    // E200 - 책검색관련 에러
    REMOTE_API_CALL_ERROR("E201", "책검색 API 호출에 실패했습니다."),

    // E900 - 공통 서버에러 메세지 코드
    INTERNAL_SERVER_ERROR("E900", "서비스처리 도중 오류가 발생했습니다.계속 발생시 관리자에게 문의하시기 바랍니다.")

    ;

    private final String code;
    private final String message;

}
