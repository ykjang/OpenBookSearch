package com.yorath.booksearch.exception;

import com.yorath.booksearch.common.ApiResultStatus;
import org.springframework.http.HttpStatus;

public class OpenApiCallException extends ServiceException {


    public OpenApiCallException(ApiResultStatus apiResultStatus) {
        super(apiResultStatus);
    }

    public OpenApiCallException(ApiResultStatus apiResultStatus, HttpStatus statusCode) {
        super(apiResultStatus, statusCode);
    }
}
