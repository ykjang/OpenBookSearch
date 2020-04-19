package com.yorath.booksearch.domain.repository;

import com.yorath.booksearch.domain.Members;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class MembersRepositoryTest {

    @Autowired MembersRepository membersRepository;

    private Members members;

    @BeforeEach
    void setUp() {
        members = Members.builder()
                .userId("testuser1")
                .password( BCrypt.hashpw("password", BCrypt.gensalt()))
                .build();
    }


    @Test
    @DisplayName("회원등록 및 비밀번호 암호화 테스트")
    void saveMember() {
        // when
        Members saveMember = membersRepository.save(members);

        // then
        assertEquals("testuser1", saveMember.getUserId(), "정상등록 여부");
        assertNotNull(saveMember.getCreatedDate(), "생성일자 정상등록 여부");
        assertTrue(BCrypt.checkpw("password", saveMember.getPassword()), "비밀번호 암호화 여부");
    }

}