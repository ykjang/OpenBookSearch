package com.yorath.booksearch.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

@Getter
@Setter
@ToString
public class CommonResponseDto<D> {

    private String code;
    private String message;
    private D data;

    public void setSuccess(@Nullable D data) {

        this.code = CommonResultStatus.SUCCESS_REQUEST.getCode();
        this.message = CommonResultStatus.SUCCESS_REQUEST.getMessage();
        this.data = data;
    }
}
