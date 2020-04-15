package com.yorath.booksearch.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.yorath.booksearch.common.CommonUtils;
import com.yorath.booksearch.domain.Members;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class JoinMemberDto {

    @NotBlank(message = "ID는 필수항목입니다.")
    private String userId;
    @NotBlank(message = "비밀번호는 필수항목입니다.")
    private String password;

    public Members toDomainEntity() {
        // 비밀번호 암호화
        String salt = CommonUtils.generateSalt();
        String encPassword = CommonUtils.encryptSHA256(password, salt);

        return Members.builder()
                .userId(userId)
                .salt(salt)
                .password(encPassword)
                .build();
    }
}
