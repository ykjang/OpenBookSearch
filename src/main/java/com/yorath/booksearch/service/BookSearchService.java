package com.yorath.booksearch.service;


import com.yorath.booksearch.dto.KeywordSummeryDto;
import com.yorath.booksearch.dto.MyBookSearchHistoryDto;

import java.util.List;

public interface BookSearchService {

    /**
     * 나의 검색 히스토리 리스트 조회
     * @param userId    회원 ID
     * @return
     */
    List<MyBookSearchHistoryDto> getMyBookSearchHistoryList(String userId);

    /**
     * 검색 키워드 TOP10 리스트 조회
     * @return
     */
    List<KeywordSummeryDto> getTop10KeywordList();


    /**
     * 나의 검색 히스토리 저장
     * @param userId
     * @param keyword
     * @return
     */
    void saveMyBookSearchHistory(String userId, String keyword);
}
