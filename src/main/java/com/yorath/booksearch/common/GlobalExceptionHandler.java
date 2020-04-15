package com.yorath.booksearch.common;

import com.yorath.booksearch.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 서비스 예외처리 예외처리
     * @param ex
     * @return
     */
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<CommonResponseDto> handleServiceException(ServiceException ex) {
        log.error("ServiceException Occurred - {}", ex.getCode() + "," + ex.getMessage());

        CommonResponseDto errorResponse = new CommonResponseDto();
        errorResponse.setCode(ex.getCode());
        errorResponse.setMessage(ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }

    /**
     * 서버에러
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponseDto> handleException(Exception ex) {
        log.error("Internal Server Error Exception");
        log.error(ex.getMessage());

        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));

        ex.printStackTrace();

        CommonResponseDto errorResponse = new CommonResponseDto();
        errorResponse.setCode(CommonResultStatus.ERROR_INTERNAL_SERVER.getCode());
        errorResponse.setMessage(CommonResultStatus.ERROR_INTERNAL_SERVER.getMessage());


        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
