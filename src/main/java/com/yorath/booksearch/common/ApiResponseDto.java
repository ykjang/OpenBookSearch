package com.yorath.booksearch.common;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

@Getter @Setter
@ToString
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ApiResponseDto<D> {

    private String code;
    private String message;
    private D data;

    public void setStatus(ApiResultStatus resultStatus) {
        this.code = resultStatus.getCode();
        this.message = resultStatus.getMessage();
    }

    public void setSuccess(@Nullable D data) {
        this.code = ApiResultStatus.REQUEST_SUCCESS.getCode();
        this.message = ApiResultStatus.REQUEST_SUCCESS.getMessage();
        this.data = data;
    }
}
