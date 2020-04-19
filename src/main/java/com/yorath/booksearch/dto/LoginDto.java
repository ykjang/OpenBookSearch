package com.yorath.booksearch.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class LoginDto {

    @NotBlank(message = "ID는 필수항목입니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수항목입니다.")
    private String password;
}
