package com.yorath.booksearch.domain.repository;

import com.yorath.booksearch.domain.BookSearchHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookSearchHistoryRepositoryTest {

    @Autowired
    BookSearchHistoryRepository historyRepository;

    @Autowired
    SearchKeywordSummeryRepository keywordSummeryRepository;

    private String userId;
    private List<BookSearchHistory> bookSearchHistoryList;  // 검색키워드 리스트

    @BeforeEach
    void setUp() {
        userId = "testuser1";
        bookSearchHistoryList = Arrays.asList(
                BookSearchHistory.builder().userId(userId).keyword("java").build(),
                BookSearchHistory.builder().userId(userId).keyword("spring").build(),
                BookSearchHistory.builder().userId(userId).keyword("jpa").build()
        );
    }

    @Test
    @DisplayName("책검색 이력등록 레파지토리 테스트")
    void saveHistoryTest() {
        // when
        BookSearchHistory savedBookSearchHistory = historyRepository.save(bookSearchHistoryList.get(0));

        // then
        assertEquals("java", savedBookSearchHistory.getKeyword());
        assertEquals(userId, savedBookSearchHistory.getUserId());

    }

    @Test
    @DisplayName("사용자별 이력조회 레파지토리 테스트 - 최신일자순")
    void findByUserIdOrderByCreatedDateDesc() {
        // given - 최신정력순 검증을 위해 n건 등록
        saveHistoryList();

        //when
        List<BookSearchHistory> resultList = historyRepository.findByUserIdOrderByCreatedDateDesc(userId);

        // then
        assertEquals(3, resultList.size(), "조회건수 일치"); // 조회건수 확인
        assertEquals("jpa", resultList.get(0).getKeyword(), "최신일자 정력여부 확인");    // 최신일자순 확인
    }

    private void saveHistoryList() {
        for (BookSearchHistory history : bookSearchHistoryList) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            historyRepository.save(history);
        }
    }
}