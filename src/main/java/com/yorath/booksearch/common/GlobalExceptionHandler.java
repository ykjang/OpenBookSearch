package com.yorath.booksearch.common;

import com.yorath.booksearch.exception.ServiceException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Request 요청객체 유효성 검증 실패시 발생하는 예외처리  - @Valid를 사용하는 객체
     *  - @RequestBody @RequestParam
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        log.error("요청항목 유효성검증 실패: {}", e.getMessage());
        ApiResponseDto errorResponse = new ApiResponseDto();
        errorResponse.setStatus(ApiResultStatus.REQUEST_PARAM_INVALID);
        errorResponse.setData(FieldError.of(e.getBindingResult()));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * 지원하지 않은 HTTP method 호출시 예외처리
     * @param e
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ApiResponseDto> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {

        log.error("지원하지 않는 메서드 요청: {}", e.getMessage());
        ApiResponseDto errorResponse = new ApiResponseDto();
        errorResponse.setStatus(ApiResultStatus.REQUEST_METHOD_NOT_ALLOWED);

        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * 비즈니스 서비스 예외처리
     * @param ex
     * @return
     */
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ApiResponseDto> handleServiceException(ServiceException ex) {
        log.error("ServiceException Occurred - {}", ex.getMessage());

        ApiResponseDto errorResponse = new ApiResponseDto();
        errorResponse.setStatus(ex.getApiResultStatus());

        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    /**
     * 서버에러 - 핸들러에 처리되지 않는 예외의 처리
     * 예상하지 못하는 예외이기 때문에 공통된 메세지로 클라이언트에 전달
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto> handleException(Exception ex) {
        log.error("서버에러 발생: {}", ex.getMessage());
        ApiResponseDto errorResponse = new ApiResponseDto();
        errorResponse.setSuccess(ApiResultStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 요청객체 유효성에러 정보 객체화
     */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        private FieldError(final String field, final String value, final String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        private static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }
}
