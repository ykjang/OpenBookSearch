package com.yorath.booksearch.service;

import com.yorath.booksearch.dto.JoinMemberDto;
import com.yorath.booksearch.dto.MemberDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("local")
class MemberServiceImplTest {

    @Autowired
    MemberService memberService;

    private String userId;
    private JoinMemberDto joinMemberDto;
    @BeforeEach
    void setUp() {

        userId = "testuser1";
        //given
        joinMemberDto = new JoinMemberDto();
        joinMemberDto.setUserId(userId);
        joinMemberDto.setPassword("password");
    }

    @Test
    @DisplayName("회원등록 서비스 테스트")
    void registMember() {

        // when
        String actualUserId = memberService.registMember(joinMemberDto);

        // then
        assertEquals(joinMemberDto.getUserId(), actualUserId,  "회원등록 서비스 - 등록회원ID 일치여부 확인 ");
    }


    @Test
    @DisplayName("회원조회 서비스 테스트")
    void findMemberTest() {

        // given - 회원등록
        memberService.registMember(joinMemberDto);

        // when
        MemberDto memberDto = memberService.findMember(userId).get();

        // then
        assertEquals(userId, memberDto.getUserId());
    }

}