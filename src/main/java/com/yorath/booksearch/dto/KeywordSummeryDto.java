package com.yorath.booksearch.dto;

import com.yorath.booksearch.domain.SearchKeywordSummery;
import lombok.Getter;

@Getter
public class KeywordSummeryDto {

    private String keyword;
    private long searchCount;

    /**
     * 키워드 검색횟수 집계 Domain 엔티티를 인자로 받아 DTO 객체로 변환하는  생성자 메서드
     * @param keywordSummery
     */
    public KeywordSummeryDto(SearchKeywordSummery keywordSummery) {
        this.keyword = keywordSummery.getKeyword();
        this.searchCount = keywordSummery.getSearchCount();
    }
}
