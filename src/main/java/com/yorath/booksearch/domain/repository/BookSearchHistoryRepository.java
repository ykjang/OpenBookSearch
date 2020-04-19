package com.yorath.booksearch.domain.repository;

import com.yorath.booksearch.domain.BookSearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookSearchHistoryRepository extends JpaRepository<BookSearchHistory, Long> {

    /**
     * 나의 검색 히스토리 조회 (최신일자 순)
     * @param userId 회원ID
     * @return
     */
    List<BookSearchHistory> findByUserIdOrderByCreatedDateDesc(String userId);
}
