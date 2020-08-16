package com.yorath.booksearch.domain.repository;

import com.yorath.booksearch.domain.Members;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class MembersRepositoryTest {

    @Autowired MembersRepository membersRepository;

    private List<Members> newMembers;

    @BeforeEach
    void setUp() {

        newMembers = Arrays.asList(
                Members.builder()
                        .userId("testuser1")
                        .password(BCrypt.hashpw("1111", BCrypt.gensalt()))
                        .build(),
                Members.builder()
                        .userId("testuser2")
                        .password(BCrypt.hashpw("2222", BCrypt.gensalt()))
                        .build()
        );

    }


    @Test
    @DisplayName("회원등록 및 비밀번호 암호화 테스트")
    void saveMember() {
        // when
        List<Members> saveMemberList = membersRepository.saveAll(newMembers);

        // then
        assertEquals(2, saveMemberList.size(), "정상등록 여부");
        assertEquals("testuser1", saveMemberList.get(0).getUserId(), "정상등록 여부");
        assertEquals("testuser2", saveMemberList.get(1).getUserId(), "정상등록 여부");
        assertNotNull(saveMemberList.get(0).getCreatedDate(), "생성일자 정상등록 여부");
        assertTrue(BCrypt.checkpw("1111", saveMemberList.get(0).getPassword()), "비밀번호 암호화 여부");
    }

    @Test
    @DisplayName("회원검색 다이나믹 쿼리 테스트")
    void findMemberDynamicQuery() {

        // given
        membersRepository.saveAll(newMembers);

        Set<String> userIds = new HashSet<>();
        userIds.add("testuser1");
        userIds.add("testuser2");
        userIds.add("testuser3");
        userIds.add("testuser4");

        // when
        List<Members> userList = membersRepository.findByUserIdAndPassword(userIds);

        // then
        assertEquals(2, userList.size());




    }

}