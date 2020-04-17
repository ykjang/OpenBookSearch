package com.yorath.booksearch.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.yorath.booksearch.domain.Members;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

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
        // 비밀번호 단방향 암호화
        String encPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        return Members.builder()
                .userId(userId)
                .password(encPassword)
                .build();
    }
}
