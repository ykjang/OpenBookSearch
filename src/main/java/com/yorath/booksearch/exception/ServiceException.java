package com.yorath.booksearch.exception;


import com.yorath.booksearch.common.CommonResultStatus;
import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {

    private String code;
    private String message;

    public ServiceException(CommonResultStatus commonResultStatus) {
        this.code = commonResultStatus.getCode();
        this.message = commonResultStatus.getMessage();
    }
}
