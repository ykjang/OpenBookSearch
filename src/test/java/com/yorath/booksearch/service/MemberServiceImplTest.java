package com.yorath.booksearch.service;

import com.yorath.booksearch.dto.JoinMemberDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
class MemberServiceImplTest {

    @Autowired
    MemberService memberService;


    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("회원등록 서비스  테스트")
    void registMember() {

        //then
        String userId = memberService.registMember(JoinMemberDto.builder()
                .userId("user1")
                .password("password")
                .build());
        
        assertEquals(userId, "user1");
    }


    @Test
    void findMember() {

    }


}