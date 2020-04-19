package com.yorath.booksearch.exception;


import com.yorath.booksearch.common.ApiResultStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class ServiceException extends RuntimeException {

    private HttpStatus httpStatus;
    private ApiResultStatus apiResultStatus;

    public ServiceException(ApiResultStatus apiResultStatus) {
        this.apiResultStatus = apiResultStatus;
    }

    public ServiceException(ApiResultStatus apiResultStatus, HttpStatus httpStatus) {
        this.apiResultStatus = apiResultStatus;
        this.httpStatus = httpStatus;
    }
}
