package com.yorath.booksearch.service;

import com.yorath.booksearch.domain.BookSearchHistory;
import com.yorath.booksearch.domain.SearchKeywordSummery;
import com.yorath.booksearch.domain.repository.BookSearchHistoryRepository;
import com.yorath.booksearch.domain.repository.SearchKeywordSummeryRepository;
import com.yorath.booksearch.dto.KeywordSummeryDto;
import com.yorath.booksearch.dto.MyBookSearchHistoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("local")
class BookSearchServiceImplTest {

    @InjectMocks
    BookSearchServiceImpl bookSearchService;

    @Mock
    private BookSearchHistoryRepository bookSearchHistoryRepository;
    @Mock
    private SearchKeywordSummeryRepository summeryRepository;


    private List<BookSearchHistory> bookSearchHistoryList;
    private List<SearchKeywordSummery> keywordSummeryList;
    private String userId;

    @BeforeEach
    void setUp() {
        // given
        userId = "testuser1";
        bookSearchHistoryList = Arrays.asList(
                BookSearchHistory.builder().userId(userId).keyword("java").build(),
                BookSearchHistory.builder().userId(userId).keyword("spring").build(),
                BookSearchHistory.builder().userId(userId).keyword("jpa").build()
        );

        keywordSummeryList = Arrays.asList(
                SearchKeywordSummery.builder().keyword("java").searchCount(5).build(),
                SearchKeywordSummery.builder().keyword("spring").searchCount(4).build(),
                SearchKeywordSummery.builder().keyword("jpa").searchCount(3).build()
        );
    }

    @Test
    @DisplayName("책검색 이력조회 서비스 테스트")
    void getMyBookSearchHistoryList() {

        // given
        when(bookSearchHistoryRepository.findByUserIdOrderByCreatedDateDesc(userId))
                .thenReturn(bookSearchHistoryList);

        List<MyBookSearchHistoryDto> expectList = bookSearchHistoryList.stream()
                .map(bookSearchHistory -> new MyBookSearchHistoryDto(bookSearchHistory))
                .collect(Collectors.toList());

        // when
        List<MyBookSearchHistoryDto> resultList = bookSearchService.getMyBookSearchHistoryList(userId);

        // then - 책검색 조회 레파지토리에서 전달한 도메인 객체가 DTO로 전환이 정상적으로 처리되었는지 확인
        assertEquals(expectList.get(0).getKeyword(), resultList.get(0).getKeyword());
        assertEquals(expectList.get(1).getKeyword(), resultList.get(1).getKeyword());
        assertEquals(expectList.get(2).getKeyword(), resultList.get(2).getKeyword());
    }

    @Test
    @DisplayName("검색 키워드 TOP10조회 서비스 테스트")
    void getTop10KeywordList() {

        // given
        when(summeryRepository.findTop10ByOrderBySearchCountDesc()).thenReturn(keywordSummeryList);

        List<KeywordSummeryDto> expectList =  keywordSummeryList.stream()
                .map(keywordSummery -> new KeywordSummeryDto(keywordSummery))
                .collect(Collectors.toList());

        // when
        List<KeywordSummeryDto> resultList = bookSearchService.getTop10KeywordList();

        // then
        assertEquals(expectList.get(0).getKeyword(), resultList.get(0).getKeyword());
        assertEquals(expectList.get(0).getSearchCount(), resultList.get(0).getSearchCount());

        assertEquals(expectList.get(1).getKeyword(), resultList.get(1).getKeyword());
        assertEquals(expectList.get(1).getSearchCount(), resultList.get(1).getSearchCount());

        assertEquals(expectList.get(2).getKeyword(), resultList.get(2).getKeyword());
        assertEquals(expectList.get(2).getSearchCount(), resultList.get(2).getSearchCount());
    }
}