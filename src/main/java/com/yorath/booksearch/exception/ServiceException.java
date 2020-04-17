package com.yorath.booksearch.exception;


import com.yorath.booksearch.common.ApiResultStatus;
import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {

    private String code;
    private String message;

    public ServiceException(ApiResultStatus apiResultStatus) {
        this.code = apiResultStatus.getCode();
        this.message = apiResultStatus.getMessage();
    }
}
