package com.yorath.booksearch.domain.repository;

import com.yorath.booksearch.domain.SearchKeywordSummery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class SearchKeywordSummeryRepositoryTest {

    @Autowired SearchKeywordSummeryRepository summeryRepository;

    private List<SearchKeywordSummery> summeryList;

    @BeforeEach
    void setUp() {
        summeryList = Arrays.asList(
                SearchKeywordSummery.builder().keyword("a1").searchCount(1).build(),    // 최하위  검색 키워드
                SearchKeywordSummery.builder().keyword("a2").searchCount(2).build(),
                SearchKeywordSummery.builder().keyword("a3").searchCount(3).build(),
                SearchKeywordSummery.builder().keyword("a4").searchCount(4).build(),
                SearchKeywordSummery.builder().keyword("a5").searchCount(5).build(),
                SearchKeywordSummery.builder().keyword("a6").searchCount(5).build(),
                SearchKeywordSummery.builder().keyword("a7").searchCount(7).build(),
                SearchKeywordSummery.builder().keyword("a8").searchCount(8).build(),
                SearchKeywordSummery.builder().keyword("a9").searchCount(9).build(),
                SearchKeywordSummery.builder().keyword("a10").searchCount(10).build(),  // 최우선순위 검색 키워드
                SearchKeywordSummery.builder().keyword("a11").searchCount(11).build()   // TOP10 에서 제외
        );
        summeryRepository.saveAll(summeryList);
    }

    @Test
    @DisplayName("검색 키워드 TOP10 목록조회 레파지토리 테스트")
    void findTop10ByOrderBySearchCountDesc() {

        // when
        List<SearchKeywordSummery> resultList =  summeryRepository.findTop10ByOrderBySearchCountDesc();

        // then
        assertEquals(10, resultList.size(), "TOP10 건수 확인");    //
        assertEquals("a11", resultList.get(0).getKeyword(), "최우선순위 검색 키워드 확인(최다조회)");    //
        assertEquals("a2", resultList.get(resultList.size()-1).getKeyword(), "최하위 검색키워드 확인 (최소 조회)");   //
    }
}