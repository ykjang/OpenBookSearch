package com.yorath.booksearch.dto;


import com.yorath.booksearch.domain.Members;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Getter
public class MemberDto {

    private String userId;
    private String password;
    private String createDate;

    /**
     * 회원정보 Domain 엔티티를 인자로 받아 회원정보 DTO 객체로 생성하는 생성자 메서드
     * @param members
     */
    public MemberDto(Members members) {
        userId = members.getUserId();
        password = members.getPassword();
        createDate = convertDateTimeToString(members.getCreatedDate());
    }


    /**
     * 로컬데이트타입 -> String 으로 변환
     * @param localDateTime
     * @return
     */
    private String convertDateTimeToString(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Optional.ofNullable(localDateTime)
                .map(formatter::format)
                .orElse("");
    }
}
