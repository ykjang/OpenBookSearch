package com.yorath.booksearch.domain.repository;

import com.yorath.booksearch.domain.SearchKeywordSummery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SearchKeywordSummeryRepository extends JpaRepository<SearchKeywordSummery, Long> {

    /**
     * 검색 키워드 집계 조회
     * @param keyword 검색 키워드
     * @return
     */
    Optional<SearchKeywordSummery> findFirstByKeyword(String keyword);

    /**
     * 검색 키워드 TOP10 조회
     * @return
     */
    List<SearchKeywordSummery> findTop10ByOrderBySearchCountDesc();
}
